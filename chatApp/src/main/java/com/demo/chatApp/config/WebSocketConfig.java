package com.demo.chatApp.config;

import com.demo.chatApp.security.JwtHandshakeInterceptor;
import org.jetbrains.annotations.NotNull;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.*;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    private final JwtHandshakeInterceptor jwtHandshakeInterceptor;
    private final CustomHandshakeHandler customHandshakeHandler;

    public WebSocketConfig(JwtHandshakeInterceptor jwtHandshakeInterceptor,
                           CustomHandshakeHandler customHandshakeHandler) {
        System.out.println("WebSocketConfig constructor called");
        this.jwtHandshakeInterceptor = jwtHandshakeInterceptor;
        this.customHandshakeHandler = customHandshakeHandler;
    }

    @Override
    public void registerStompEndpoints(@NotNull StompEndpointRegistry registry) {

        System.out.println("websocketconfig" + registry);
        registry.addEndpoint("/ws")
                .setAllowedOrigins("*")
                .addInterceptors(jwtHandshakeInterceptor)
                .setHandshakeHandler(customHandshakeHandler);

    }

    @Override
    public void configureMessageBroker(@NotNull MessageBrokerRegistry config) {
        config.enableSimpleBroker("/user");
        config.setUserDestinationPrefix("/user");
        config.setApplicationDestinationPrefixes("/app");
    }

}
