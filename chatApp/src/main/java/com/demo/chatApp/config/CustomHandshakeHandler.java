package com.demo.chatApp.config;

import com.demo.chatApp.model.User;
import com.demo.chatApp.repository.UserRepository;
import com.demo.chatApp.security.JwtTokenUtil;
import com.demo.chatApp.security.Principal.CustomPrincipal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.support.DefaultHandshakeHandler;

import java.security.Principal;
import java.util.Map;
import java.util.Optional;

@Component
public class CustomHandshakeHandler extends DefaultHandshakeHandler {

    private final JwtTokenUtil jwtTokenUtil;

    private final UserRepository userRepository;

    @Autowired
    public CustomHandshakeHandler(JwtTokenUtil jwtTokenUtil, UserRepository userRepository){
        this.jwtTokenUtil = jwtTokenUtil;
        this.userRepository = userRepository;
    }
    @Override
    protected Principal determineUser(ServerHttpRequest request,
                                      WebSocketHandler wsHandler,
                                      Map<String, Object> attributes) {

        String query = request.getURI().getQuery();
        System.out.println("WebSocket Query: " + query);
        if (query == null || !query.contains("token=")) {
            return null;
        }

        String token = request.getURI().getQuery().split("token=")[1].trim();

        if (!jwtTokenUtil.validateToken(token)) {
            System.out.println("Invalid token received during WebSocket handshake");
            return null;
        }

        String username = jwtTokenUtil.getUsernameFromToken(token);
        Optional<User> user = userRepository.findByUsername(username);

        if (user.isPresent()) {
            System.out.println("WebSocket authenticated as: " + username);
            return new CustomPrincipal(username, user.get().getId());
        } else {
            System.out.println("User not found for username: " + username);
            return null;
        }
    }

}
