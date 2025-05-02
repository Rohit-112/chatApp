package com.demo.chatApp.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Base64;
import java.util.Date;

@Component
public class JwtTokenUtil {

    @Value("${jwt.secret}")
    private String secretKeyBase64;

    @PostConstruct
    public void logSecretKey() {
        System.out.println("Secret Key: " + secretKeyBase64);
    }

    public SecretKey getSecretKey(){
        byte[] decodeKey = Base64.getDecoder().decode(secretKeyBase64);
        return Keys.hmacShaKeyFor(decodeKey);
    }

    public String generateToken(String username) {
        System.out.println("Hello: " + username);
        // FOR 1 HOUR
        long EXPIRATION_TIME = 1000 * 60 * 60;
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(getSecretKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public boolean validateToken(String token){
        try {
            Jwts.parserBuilder()
                    .setSigningKey(getSecretKey())
                    .build()
                    .parseClaimsJws(token);
            return true;
        }catch (JwtException e){
            System.out.println("Token invalid" + e.getMessage());
            return false;
        }
    }

    public String getUsernameFromToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSecretKey() )
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }
}
