package com.example.springboilerplate.config;

import com.example.springboilerplate.filters.JWTFilter;
import com.example.springboilerplate.filters.LoginFilter;
import com.example.springboilerplate.utils.JWT;
import com.example.springboilerplate.utils.ResponseFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

import java.util.Collections;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final AuthenticationConfiguration authenticationConfiguration;
    private final JWT jwt;
    private final ResponseFactory responseFactory;

    public SecurityConfig(AuthenticationConfiguration authenticationConfiguration, JWT jwt, ResponseFactory responseFactory) {
        this.authenticationConfiguration = authenticationConfiguration;
        this.jwt = jwt;
        this.responseFactory = responseFactory;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf(csrf -> csrf.disable());
        http.formLogin(formLogin -> formLogin.disable());
        http.httpBasic(httpBasic -> httpBasic.disable());

        // 페이지별 권한 설정
        http.authorizeHttpRequests(auth -> auth
                .requestMatchers("/", "/login", "/join", "/swagger-ui/**", "/v3/api-docs/**").permitAll()
                .requestMatchers("/admin").hasRole("ADMIN")
                .anyRequest().authenticated()
        );

        // Filter 설정
        // 1. JWTFilter: JWT 토큰을 검증하는 필터
        // 2. LoginFilter: 로그인 요청을 처리하는 필터
        // addFilterBefore: UsernamePasswordAuthenticationFilter 앞에 JWTFilter 추가
        // addFilterAt: UsernamePasswordAuthenticationFilter 위치에 LoginFilter 추가
        // UsernamePasswordAuthenticationFilter: 사용자 이름(email)/암호 인증을 처리하는 인증 필터
        http.addFilterBefore(new JWTFilter(this.jwt), UsernamePasswordAuthenticationFilter.class);
        //http.addFilterAt(new LoginFilter(this.authenticationManager(this.authenticationConfiguration), this.jwt, responseUtils), UsernamePasswordAuthenticationFilter.class);
        http.addFilterAt(new LoginFilter(authenticationManager(authenticationConfiguration), jwt, responseFactory), UsernamePasswordAuthenticationFilter.class);

        // 세션 설정
        http.sessionManagement(session -> session
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        // CORS 설정
        http.cors(cors -> cors.configurationSource(request -> {
            CorsConfiguration config = new CorsConfiguration();
            config.setAllowedOrigins(Collections.singletonList("http://localhost:5000"));
            config.setAllowedMethods(Collections.singletonList("*"));
            config.setAllowCredentials(true);
            config.setAllowedHeaders(Collections.singletonList("*"));
            config.setMaxAge(3600L);
            config.setExposedHeaders(Collections.singletonList("Authorization"));
            return config;
        }));

        return http.build();
    }
}
