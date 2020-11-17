package com.insurance.solutions.app.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.insurance.solutions.app.services.UserDetailsServiceImplementation;
import com.insurance.solutions.app.services.UserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import static com.insurance.solutions.app.security.SecurityConstants.*;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(
        securedEnabled = true
)
public class WebSecurity extends WebSecurityConfigurerAdapter {
    private final UserDetailsServiceImplementation userDetailsService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final UserService userService;
    private final ObjectMapper objectMapper;

    public WebSecurity(
            UserDetailsServiceImplementation userDetailsService,
            BCryptPasswordEncoder bCryptPasswordEncoder,
            UserService userService,
            ObjectMapper objectMapper
    ) {
        this.userDetailsService = userDetailsService;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.userService = userService;
        this.objectMapper = objectMapper;
    }

    /**
     * This method define which resources are public and which are secured.
     * In our case, we set the SIGN_UP_URL, ALL_USERS, HEALTHCHECK, and swagger configs endpoints as being public and everything else as being secured.
     * We also configure CORS (Cross-Origin Resource Sharing) support through http.cors()
     * and we add a custom security filter in the Spring Security filter chain.
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.cors().and().csrf().disable().authorizeRequests()
                .antMatchers(
                        HEALTHCHECK,
                        // Example
                        CRUD_BOOKS,

                        //  Swagger configs
                        SWAGGER,
                        WEBJARS,
                        V2API,
                        UICONFIG,
                        SWAGGERRESOURCES,
                        SECURITYCONFIG
                ).permitAll()

                .antMatchers(INSURANCE_COMPANY).hasRole("ADMIN")
                .antMatchers(USERS).hasRole("ADMIN")

                .anyRequest().authenticated()
                .and()
                .addFilter(new JWTAuthenticationFilter(authenticationManager(), userService, objectMapper))
                .addFilter(new JWTAuthorizationFilter(authenticationManager(), userDetailsService))
                // this disables session creation on Spring Security
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(bCryptPasswordEncoder);
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration corsConfiguration = new CorsConfiguration().applyPermitDefaultValues();
        corsConfiguration.addAllowedMethod("PUT");
        corsConfiguration.addAllowedMethod("DELETE");
        corsConfiguration.addAllowedMethod("GET");
        corsConfiguration.addAllowedMethod("POST");
        source.registerCorsConfiguration("/**", corsConfiguration);
        return source;
    }
}
