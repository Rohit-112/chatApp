package com.demo.chatApp.config;

import com.demo.chatApp.security.JwtHandshakeInterceptor;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Autowired
    private CustomHandshakeHandler customHandshakeHandler;

    @Autowired
    private JwtHandshakeInterceptor jwtHandshakeInterceptor;


    @Override
    public void registerStompEndpoints(@NonNull StompEndpointRegistry registry) {
        System.out.println("Registering WebSocket endpoint...");
        registry.addEndpoint("/ws-chat")
                .setHandshakeHandler(customHandshakeHandler)
                .addInterceptors(jwtHandshakeInterceptor)
                .setAllowedOriginPatterns("*")
                .withSockJS();
        System.out.println("WebSocketConfig: intercepting handshake");
    }

    @Override
    public void configureMessageBroker(@NonNull MessageBrokerRegistry config) {
        System.out.println("WebSocketConfig loaded");
        config.enableSimpleBroker("/topic/","/queue/");
        config.setApplicationDestinationPrefixes("/app");
        config.setUserDestinationPrefix("/user");
    }
}
