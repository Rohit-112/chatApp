package com.demo.chatApp.security.Principal;

import java.security.Principal;

public class CustomPrincipal implements Principal {

    private final String name;
    private final Long userId;

    public CustomPrincipal(String name, Long userId) {
        this.name = name;
        this.userId = userId;
    }

    @Override
    public String getName() {
        return name;
    }

    public Long getUserId() {
        return userId;
    }
}
