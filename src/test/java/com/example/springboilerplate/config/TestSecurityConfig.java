package com.example.springboilerplate.config;

import com.example.springboilerplate.filters.NoOpFilter;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.example.springboilerplate.utils.JsonResponseFactory;
import com.example.springboilerplate.utils.ResponseFactory;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@TestConfiguration
@EnableWebSecurity
public class TestSecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(csrf -> csrf.disable())
                .authorizeRequests(auth -> auth.anyRequest().permitAll())
                .addFilterBefore(new NoOpFilter(), UsernamePasswordAuthenticationFilter.class); // NoOpFilter로 보안 우회

        return http.build();
    }

    @Bean
    public ResponseFactory responseFactory() {
        return new JsonResponseFactory(new ObjectMapper());
    }
}
