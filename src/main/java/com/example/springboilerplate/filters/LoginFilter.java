package com.example.springboilerplate.filters;

import com.example.springboilerplate.dto.response.ErrorResponseDTO;
import com.example.springboilerplate.dto.response.SuccessResponseDTO;
import com.example.springboilerplate.dto.user.AppUserDetails;
import com.example.springboilerplate.utils.JWT;
import com.example.springboilerplate.utils.ResponseFactory;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;

public class LoginFilter extends UsernamePasswordAuthenticationFilter {
    private static final Logger logger = LoggerFactory.getLogger(LoginFilter.class);

    private final AuthenticationManager authenticationManager;
    private final JWT jwt;
    private final ResponseFactory responseFactory;

    public LoginFilter(AuthenticationManager authenticationManager, JWT jwt, ResponseFactory responseFactory){
        this.authenticationManager = authenticationManager;
        this.jwt = jwt;
        this.responseFactory = responseFactory;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        String email = obtainEmail(request);
        String password = obtainPassword(request);
        logger.info(email + " login!");
        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(email, password, null);
        return authenticationManager.authenticate(authToken);
    }

    private String obtainEmail(HttpServletRequest request) {
        return request.getParameter("email");
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authentication) throws IOException {
        logger.info("로그인 성공");
        AppUserDetails customUserDetails = (AppUserDetails) authentication.getPrincipal();
        String username = customUserDetails.getUsername();
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        Iterator<? extends GrantedAuthority> iterator = authorities.iterator();
        GrantedAuthority auth = iterator.next();
        String role = auth.getAuthority();

        String token = jwt.createJwt(username, role, 60*60*1000L);


        response.addHeader("Authorization", "Bearer " + token);

        // 성공 응답에서 토큰은 헤더에만 포함하고, 응답 본문에는 메시지와 기타 데이터를 포함
        ResponseEntity<SuccessResponseDTO<Void>> successResponse = responseFactory.createSuccessResponse(
                null, "로그인 성공");
        responseFactory.writeJsonResponse(response, successResponse.getBody(), successResponse.getStatusCode());
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException {
        logger.info("로그인 실패");
        ResponseEntity<ErrorResponseDTO> errorResponse = responseFactory.createErrorResponse(
                HttpStatus.UNAUTHORIZED, "로그인 실패: " + failed.getMessage(), request.getRequestURI());
        responseFactory.writeJsonResponse(response, errorResponse.getBody(), errorResponse.getStatusCode());
    }
}
