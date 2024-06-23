package com.example.springboilerplate.filters;

import io.jsonwebtoken.io.IOException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.filter.OncePerRequestFilter;

public class NoOpFilter extends OncePerRequestFilter {
private static final Logger logger = LoggerFactory.getLogger(NoOpFilter.class);
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException, java.io.IOException {
        logger.info("NoOpFilter");
        // 아무 동작도 하지 않고 다음 필터로 넘어갑니다.
        filterChain.doFilter(request, response);
    }
}