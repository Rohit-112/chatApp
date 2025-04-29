package com.demo.chatApp.config;

import org.jetbrains.annotations.NotNull;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.support.DefaultHandshakeHandler;

import java.security.Principal;
import java.util.Map;
import java.util.UUID;

public class CustomHandshakeHandler extends DefaultHandshakeHandler {

    @Override
    protected Principal determineUser(ServerHttpRequest request,
                                      @NotNull WebSocketHandler wsHandler,
                                      @NotNull Map<String, Object> attributes) {
        String uri = request.getURI().toString();
        String user = uri.contains("user=") ?
                uri.substring(uri.indexOf("user=") + 5).split("&")[0] :
                "anon-" + UUID.randomUUID();

        return () -> user;
    }
}
