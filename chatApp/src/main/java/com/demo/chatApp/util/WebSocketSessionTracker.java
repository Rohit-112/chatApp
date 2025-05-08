package com.demo.chatApp.util;

import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class WebSocketSessionTracker {

    private final Set<String> connectedUsers = ConcurrentHashMap.newKeySet();

    public void adduser(String username){
        connectedUsers.add(username);
    }

    public void removeUser(String username){
        connectedUsers.remove(username);
    }

    public boolean isOnline(String username){
        return connectedUsers.contains(username);
    }

    public Set<String> getOnlineUsers(){
        return connectedUsers;
    }
}
