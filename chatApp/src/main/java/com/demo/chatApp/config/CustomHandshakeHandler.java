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
import org.springframework.web.util.UriComponentsBuilder;

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

        String username = (String) attributes.get("username");

        if (username == null || username.isEmpty()) {
            System.out.println("[CustomHandshakeHandler] Missing username");
            return null;
        }

        System.out.println("[CustomHandshakeHandler] Creating CustomPrincipal with username: " + username);
        return new CustomPrincipal(username, null);
    }

       /* String token = UriComponentsBuilder.fromUriString(request.getURI().toString())
                .build()
                .getQueryParams()
                .getFirst("token");

        if (token == null || token.isEmpty()) {
            System.out.println("Token missing in query string.");
            return null;
        }

        if (!jwtTokenUtil.validateToken(token)) {
            System.out.println("Invalid token.");
            return null;
        }

        String username = jwtTokenUtil.getUsernameFromToken(token);
        if (username == null || username.isEmpty()) {
            System.out.println("Username is null or empty.");
            return null;
        }

        Optional<User> user = userRepository.findByUsername(username);
        if (user.isEmpty()) {
            System.out.println("User not found: " + username);
            return null;
        }

        System.out.println("Authenticated user: " + username);
        return new CustomPrincipal(username, user.get().getId());
    }*/

}
