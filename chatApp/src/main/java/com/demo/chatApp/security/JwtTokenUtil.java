package com.demo.chatApp.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import org.springframework.stereotype.Component;

@Component
public class JwtTokenUtil {

    private final String jwtSecret = "secretKey";

    public String getUsernameFromToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(jwtSecret.getBytes())
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(jwtSecret.getBytes())
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (ExpiredJwtException | UnsupportedJwtException |
                 MalformedJwtException | SignatureException |
                IllegalArgumentException ex) {
            System.out.println("JWT validation failed: " + ex.getMessage());
            return false;
        }
    }

    public String generateToken(String username) {
        String token = Jwts.builder()
                .setSubject(username)
                .signWith(Keys.hmacShaKeyFor("secretKey".getBytes()), SignatureAlgorithm.HS256)
                .compact();
        System.out.println("generated token: " + token);
        return token;
    }

    public String getUsernameToken(String token){
        System.out.println("Token received: " + token);
        return Jwts.parserBuilder()
                .setSigningKey("secretKey".getBytes())
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

}
