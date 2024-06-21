package com.example.springboilerplate.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.example.springboilerplate.utils.JsonResponseFactory;
import com.example.springboilerplate.utils.ResponseFactory;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@TestConfiguration
@EnableWebSecurity
public class TestSecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(csrf -> csrf.disable());
        http.authorizeRequests(auth -> auth.anyRequest().permitAll()); // 모든 요청 허용

        return http.build();
    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public ResponseFactory responseFactory() {
        return new JsonResponseFactory(new ObjectMapper());
    }
}
