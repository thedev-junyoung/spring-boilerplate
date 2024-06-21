package com.example.springboilerplate.filters;

import com.example.springboilerplate.dto.user.AppUserDetails;
import com.example.springboilerplate.entity.User;
import com.example.springboilerplate.utils.JWT;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.example.springboilerplate.type.UserRole;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

public class JWTFilter extends OncePerRequestFilter {
    private static final Logger logger = LoggerFactory.getLogger(JWTFilter.class);

    private final JWT jwt;

    public JWTFilter(JWT jwt) {
        this.jwt = jwt;
    }

    // 요청 헤더에서 JWT 토큰을 추출하고 검증 - 모든요청
    // 토큰이 유효하면 사용자 정보를 설정하고, SecurityContextHolder에 Authentication 객체를 설정
    // 필터 체인의 다음 필터를 호출



    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException, ServletException, IOException {
        logger.info("시작");
        //request에서 Authorization 헤더를 찾음
        String authorization= request.getHeader("Authorization");
        logger.info("authorization:"+authorization);
        //Authorization 헤더 검증 Berer zxxxx
        if (authorization == null || !authorization.startsWith("Bearer ")) {
            logger.info("토큰이 없거나 유효하지않음. doFilter 호출");
            filterChain.doFilter(request, response);

            //조건이 해당되면 메소드 종료 (필수)
            return;
        }

        String token = authorization.split(" ")[1];
        //토큰 소멸 시간 검증
        if (jwt.isExpired(token)) {
            logger.info("token expired");
            filterChain.doFilter(request, response);

            //조건이 해당되면 메소드 종료 (필수)
            return;
        }
        String email = jwt.getEmail(token);
        String role = jwt.getRole(token);
        logger.info("username:"+email);
        logger.info("role:"+role);

        // 사용자 정보를 SecurityContextHolder에 설정
        User user = new User();
        user.setEmail(email);
        user.setPassword("temppassword");
        user.setRole(UserRole.valueOf(role));

        AppUserDetails customUserDetails = new AppUserDetails(user);

        Authentication authToken = new UsernamePasswordAuthenticationToken(customUserDetails, null, customUserDetails.getAuthorities());

        SecurityContextHolder.getContext().setAuthentication(authToken);
        logger.info("filterChain.doFilter(request, response); 전");
        // 다음 필터로 요청 전달
        filterChain.doFilter(request, response);
        logger.info("끝");
    }
}
