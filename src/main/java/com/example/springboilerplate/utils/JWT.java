package com.example.springboilerplate.utils;

import io.jsonwebtoken.Jwts;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
public class JWT {
    private static final Logger logger = LoggerFactory.getLogger(JWT.class);
    private SecretKey secretKey;

    public JWT(@Value("${spring.jwt.secret}")String secret){
        logger.info(" ");
        this.secretKey = new SecretKeySpec(secret.getBytes(
                StandardCharsets.UTF_8),
                Jwts.SIG.HS256.key().build().getAlgorithm()
        );
    }

    public String getEmail(String token){
        logger.info(" ");
        return Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .get("email", String.class);

    }

    public String getRole(String token){
        logger.info(" ");
        return Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .get("role", String.class);
    }

    public Boolean isExpired(String token){
        logger.info(" ");
        return Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getExpiration().before(new Date());

    }

    public String createJwt(String username, String role, Long expiredMs) {
        logger.info(" ");
        return Jwts.builder()
                .claim("email", username)
                .claim("role", role)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + expiredMs))
                .signWith(secretKey)
                .compact();
    }
}
