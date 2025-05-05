package com.demo.chatApp.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import java.util.Map;

@Component
public class JwtHandshakeInterceptor implements HandshakeInterceptor {

    private final JwtTokenUtil jwtTokenUtil;

    @Autowired
    public JwtHandshakeInterceptor(JwtTokenUtil jwtTokenUtil){
        this.jwtTokenUtil = jwtTokenUtil;
    }

    @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response,
                                   WebSocketHandler wsHandler, Map<String, Object> attributes) {

        if (!(request instanceof ServletServerHttpRequest)) {
            return false;
        }

        String token = ((ServletServerHttpRequest) request).getServletRequest().getParameter("token");

        if (token == null || token.isEmpty()) {
            System.out.println("Missing token");
            return false;
        }

        try {
            String username = jwtTokenUtil.getUsernameFromToken(token);
            System.out.println("Username: " + jwtTokenUtil.getUsernameFromToken(token));
            if (username == null || username.isEmpty()) {
                System.out.println("Invalid token: no username");
                return false;
            }

            System.out.println("Skipping token check for debug");
            attributes.put("username", username);
            return true;

        } catch (Exception e) {
            System.out.println("Token validation failed: " + e.getMessage());
            return false;
        }
    }

    @Override
    public void afterHandshake(ServerHttpRequest request,
                               ServerHttpResponse response,
                               WebSocketHandler wsHandler,
                               Exception exception) {

    }
}
