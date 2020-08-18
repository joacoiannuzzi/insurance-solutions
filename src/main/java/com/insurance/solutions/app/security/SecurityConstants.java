package com.insurance.solutions.app.security;

public class SecurityConstants {
    public static final String SECRET = "SecretKeyToGenJWTs";
    public static final long EXPIRATION_TIME = 864_000_000; // 10 days
    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String HEADER_STRING = "Authorization";
    public static final String SIGN_UP_URL = "/users/sign-up";
    public static final String ALL_USERS = "/users/all";
    public static final String HEALTHCHECK = "/_healthcheck";

    // Swagger configuration
    public static final String SWAGGER = "/swagger-ui.html";
    public static final String WEBJARS = "/webjars/**";
    public static final String V2API = "/v2/api-docs";
    public static final String UICONFIG = "/configuration/ui";
    public static final String SWAGGERRESOURCES = "/swagger-resources/**";
    public static final String SECURITYCONFIG = "/configuration/security";
}
