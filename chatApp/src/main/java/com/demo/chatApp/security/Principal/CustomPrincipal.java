package com.demo.chatApp.security.Principal;

import java.security.Principal;

public class CustomPrincipal implements Principal {

    private final String username;
    private final Long Id;

    public CustomPrincipal(String username, Long Id){
        this.username = username;
        this.Id = Id;
    }

    @Override
    public String getName() {
        return username;
    }

    public Long getUserId(){
        return Id;
    }
}
