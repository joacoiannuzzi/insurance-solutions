package com.insurance.solutions.app;

import com.insurance.solutions.app.models.User;
import com.insurance.solutions.app.repositories.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.stream.Stream;

@SpringBootApplication
public class InsuranceSolutionsApplication {

    public static void main(String[] args) {
        SpringApplication.run(InsuranceSolutionsApplication.class, args);
    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    CommandLineRunner init(UserRepository userRepository) {
        return args -> {
            if (!userRepository.findAll().iterator().hasNext()) {
                Stream.of("Sebastian", "Tomas", "Franco", "Jose").forEach(name -> {
                    User user = new User(name, name.toLowerCase() + "@mail.com", new BCryptPasswordEncoder().encode("password"));
                    userRepository.save(user);
                });
            }
        };
    }
}
