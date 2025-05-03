package com.demo.chatApp.config;

import com.demo.chatApp.model.User;
import com.demo.chatApp.repository.UserRepository;
import com.demo.chatApp.security.JwtTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.support.DefaultHandshakeHandler;

import java.security.Principal;
import java.util.Map;
import java.util.Optional;

public class CustomHandshakeHandler extends DefaultHandshakeHandler {

    @Autowired
    private JwtTokenUtil jwtTokenUtil;
    @Autowired
    private UserRepository userRepository;

    @Override
    protected Principal determineUser(ServerHttpRequest request,
                                      WebSocketHandler wsHandler,
                                      Map<String, Object> attributes) {
        String query = request.getURI().getQuery();
        System.out.println("Websocket Query: " + query);

        if (query == null || !query.contains("token=")) {
            System.out.println("token missing in query");
            return null;
        }

        String token = query.split("token=")[1].trim();
        if (jwtTokenUtil.validateToken(token)) {
            String username = jwtTokenUtil.getUsernameFromToken(token);
            System.out.println("Authenticated WebSocket user: " + username);

            Optional<User> optionalUser = userRepository.findByUsername(username);
            if (optionalUser.isPresent()){
                Long userId = optionalUser.get().getId();
                System.out.println("User found, Websocket Principal id: " + userId);
                return userId::toString;
            }else{
                System.out.println("User not found for username: " + username);
            }
        }else{
            System.out.println("Invalid JWT token");
        }
        return null;
    }
}
