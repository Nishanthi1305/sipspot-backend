package com.example.demo.security;

import com.example.demo.config.JwtProperties;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
public class JwtUtil {

    @Autowired
    private JwtProperties jwtProperties;

    // ✅ SAFE: prevent null secret crash
    private Key getSigningKey() {
        String secret = jwtProperties.getSecret();

        if (secret == null || secret.isEmpty()) {
            throw new IllegalStateException(
                "JWT secret is missing! Please set APP_JWT_SECRET in Render environment variables."
            );
        }

        return Keys.hmacShaKeyFor(secret.getBytes());
    }

    // Generate token with username + role
    public String generateToken(String username, String role) {
        return Jwts.builder()
                .setSubject(username)
                .claim("role", role)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + jwtProperties.getExpiration()))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    // Extract username
    public String extractUsername(String token) {
        return getClaims(token).getSubject();
    }

    // Extract role
    public String extractRole(String token) {
        return getClaims(token).get("role", String.class);
    }

    // Validate token
    public boolean validateToken(String token) {
        try {
            getClaims(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    // Parse token
    private Claims getClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}