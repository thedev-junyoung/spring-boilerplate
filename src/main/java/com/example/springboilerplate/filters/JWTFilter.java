package com.example.springboilerplate.filters;

import com.example.springboilerplate.dto.response.ErrorResponseDTO;
import com.example.springboilerplate.dto.user.AppUserDetails;
import com.example.springboilerplate.entity.User;
import com.example.springboilerplate.utils.JWT;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.example.springboilerplate.type.UserRole;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class JWTFilter extends OncePerRequestFilter {
    private static final Logger logger = LoggerFactory.getLogger(JWTFilter.class);

    private final JWT jwt;

    public JWTFilter(JWT jwt) {
        this.jwt = jwt;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        logger.info("시작");

        String authorization = request.getHeader("Authorization");
        logger.info("authorization: {}", authorization);

        if (authorization == null || !authorization.startsWith("Bearer ")) {
            logger.info("토큰이 없거나 유효하지 않음");
            filterChain.doFilter(request, response);
            return;
        }

        String token = authorization.split(" ")[1];
        try {
            if (jwt.isExpired(token)) {
                throw new ExpiredJwtException(null, null, "토큰이 만료되었습니다.");
            }

            String email = jwt.getEmail(token);
            String role = jwt.getRole(token);
            logger.info("username: {}", email);
            logger.info("role: {}", role);

            User user = new User();
            user.setEmail(email);
            user.setPassword("temppassword");
            user.setRole(UserRole.valueOf(role));

            AppUserDetails customUserDetails = new AppUserDetails(user);
            Authentication authToken = new UsernamePasswordAuthenticationToken(customUserDetails, null, customUserDetails.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authToken);

            filterChain.doFilter(request, response);

        } catch (ExpiredJwtException ex) {
            logger.info("토큰이 만료되었습니다: {}", ex.getMessage());
            handleJwtException(response, HttpStatus.UNAUTHORIZED, "토큰이 만료되었습니다.");
        } catch (Exception ex) {
            logger.error("JWT 처리 중 오류 발생", ex);
            handleJwtException(response, HttpStatus.INTERNAL_SERVER_ERROR, "JWT 처리 중 오류 발생");
        }

        logger.info("끝");
    }

    private void handleJwtException(HttpServletResponse response, HttpStatus status, String message) throws IOException {
        response.setStatus(status.value());
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        Map<String, Object> errorDetails = new HashMap<>();
        errorDetails.put("error", message);

        ErrorResponseDTO errorResponse = new ErrorResponseDTO(
                status.value(),
                message,
                errorDetails
        );

        ObjectMapper objectMapper = new ObjectMapper();
        response.getWriter().write(objectMapper.writeValueAsString(errorResponse));
    }
}
