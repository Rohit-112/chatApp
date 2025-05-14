package com.demo.chatApp.config;

import com.demo.chatApp.repository.UserRepository;
import com.demo.chatApp.security.JwtTokenUtil;
import com.demo.chatApp.security.Principal.CustomPrincipal;
import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.support.DefaultHandshakeHandler;

import java.security.Principal;
import java.util.Map;

@Component
public class CustomHandshakeHandler extends DefaultHandshakeHandler {

    private final JwtTokenUtil jwtTokenUtil;
    private final UserRepository userRepository;
    private final RabbitMQConfig rabbitMQConfig;
    private final AmqpAdmin amqpAdmin;

    @Autowired
    public CustomHandshakeHandler(JwtTokenUtil jwtTokenUtil, UserRepository userRepository,
                                  RabbitMQConfig rabbitMQConfig,
                                  AmqpAdmin amqpAdmin){
        this.jwtTokenUtil = jwtTokenUtil;
        this.userRepository = userRepository;
        this.rabbitMQConfig = rabbitMQConfig;
        this.amqpAdmin = amqpAdmin;
    }

    @Override
    protected Principal determineUser(ServerHttpRequest request,
                                      WebSocketHandler wsHandler,
                                      Map<String, Object> attributes) {

        String username = (String) attributes.get("username");

        if (username == null || username.isEmpty()) {
            System.out.println("[CustomHandshakeHandler] Missing username");
            return null;
        }

        System.out.println("[CustomHandshakeHandler] Creating CustomPrincipal with username: " + username);
        rabbitMQConfig.setupQueueForUser(username, amqpAdmin);
        return new CustomPrincipal(username, null);
    }
}
