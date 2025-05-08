package com.demo.chatApp.security;

import com.demo.chatApp.util.WebSocketSessionTracker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import java.util.Map;

@Component
public class JwtHandshakeInterceptor implements HandshakeInterceptor {

    private final JwtTokenUtil jwtTokenUtil;
    @Autowired
    private WebSocketSessionTracker sessionTracker;

    @Autowired
    public JwtHandshakeInterceptor(JwtTokenUtil jwtTokenUtil) {
        this.jwtTokenUtil = jwtTokenUtil;
    }

    @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response,
                                   WebSocketHandler wsHandler, Map<String, Object> attributes) throws Exception {
        System.out.println("[HandshakeInterceptor] Interceptor triggered");

        if (!(request instanceof ServletServerHttpRequest)) {
            return false;
        }

        String token = ((ServletServerHttpRequest) request)
                .getServletRequest()
                .getParameter("token");

        if (token == null || token.isEmpty()) {
            System.out.println("[HandshakeInterceptor] Missing token");
            return false;
        }

        try {
            String username = jwtTokenUtil.getUsernameFromToken(token);
            System.out.println("[HandshakeInterceptor] Authenticated user: " + username);

            if (username == null || username.isEmpty()) {
                return false;
            }

            sessionTracker.adduser(username);

            attributes.put("username", username);
            attributes.put("token", token);
            return true;

        } catch (Exception e) {
            System.out.println("[HandshakeInterceptor] Token parsing failed: " + e.getMessage());
            return false;
        }
    }

    @Override
    public void afterHandshake(ServerHttpRequest request,
                               ServerHttpResponse response,
                               WebSocketHandler wsHandler,
                               Exception exception) {
        // No-op
    }
}
