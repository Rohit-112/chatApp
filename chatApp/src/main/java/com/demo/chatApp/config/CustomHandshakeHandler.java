package com.demo.chatApp.config;

import com.demo.chatApp.security.JwtTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.support.DefaultHandshakeHandler;

import java.security.Principal;
import java.util.Map;

public class CustomHandshakeHandler extends DefaultHandshakeHandler {

    private final JwtTokenUtil jwtTokenUtil;

    @Autowired
    public CustomHandshakeHandler(JwtTokenUtil jwtTokenUtil) {
        this.jwtTokenUtil = jwtTokenUtil;
    }

    @Override
    protected Principal determineUser(ServerHttpRequest request,
                                      WebSocketHandler wsHandler,
                                      Map<String, Object> attributes) {
        String query = request.getURI().getQuery();
        System.out.println("Query: " + query);

        if (query == null || !query.contains("token=")) {
            System.out.println("token missing in query");
            return null;
        }

        String token = query.split("token=")[1];
        if (jwtTokenUtil.validateToken(token)) {
            String username = jwtTokenUtil.getUsernameFromToken(token);
            System.out.println("Authenticated WebSocket user: " + username);
            return () -> username;
        }else{
            System.out.println("Invalid JWT");
        }
        return null;
    }
}
