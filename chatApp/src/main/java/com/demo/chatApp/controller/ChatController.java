package com.demo.chatApp.controller;

import com.demo.chatApp.model.ChatMessage;
import com.demo.chatApp.model.MessageType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
public class ChatController {

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @MessageMapping("/chat.sendMessage")
    @SendTo("/topic/public")
    public ChatMessage sendMessage(ChatMessage message){
        // cans save message to database from here
        return message;
    }

    @MessageMapping("/chat.addUser")
    @SendTo("/topic/public")
    public  ChatMessage addUser(ChatMessage message){
        message.setType(MessageType.JOIN);
        message.setContent(message.getSender() + "has joined the room");
        return message;
    }

    @MessageMapping("/chat.removeUser")
    @SendTo("/topic/public")
    public ChatMessage removeUser(ChatMessage message){
        message.setType(MessageType.LEAVE);
        message.setContent(message.getSender() + "has left the room");
        return message;
    }
}
