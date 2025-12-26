package com.example.demo.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

@Component
public class JwtTokenProvider {
    private final SecretKey key;
    private final long validityInMilliseconds = 3600000; // 1 hour
    
    public JwtTokenProvider(String secret) {
        this.key = Keys.hmacShaKeyFor(secret.getBytes());
    }
    
    public JwtTokenProvider() {
        this.key = Keys.secretKeyFor(SignatureAlgorithm.HS256);
    }
    
    public String createToken(Long userId, String email, String role) {
        Date now = new Date();
        Date validity = new Date(now.getTime() + validityInMilliseconds);
        
        return Jwts.builder()
            .setSubject(email)
            .claim("userId", userId)
            .claim("role", role)
            .setIssuedAt(now)
            .setExpiration(validity)
            .signWith(key)
            .compact();
    }
    
    public String generateToken(org.springframework.security.core.Authentication auth, Long userId, String role, String email) {
        return createToken(userId, email, role);
    }
    
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
    
    public String getEmailFromToken(String token) {
        return Jwts.parserBuilder()
            .setSigningKey(key)
            .build()
            .parseClaimsJws(token)
            .getBody()
            .getSubject();
    }
}
