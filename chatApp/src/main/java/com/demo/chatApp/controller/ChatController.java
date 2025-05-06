package com.demo.chatApp.controller;

import com.demo.chatApp.model.ChatMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.security.Principal;

@Controller("chatController")
public class ChatController {

    private final SimpMessagingTemplate messagingTemplate;

    @Autowired
    public ChatController(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    @MessageMapping("/chat.sendMessage")
    public void sendMessage(@Payload ChatMessage message, Principal principal) {
        System.out.println("Message from userId: " + principal.getName());

        // from here we can save to database
        messagingTemplate.convertAndSendToUser(
                message.getReceiver(),
                "/queue/messages",
                message
        );
        System.out.println("Received message: " + message + " from: " + principal.getName());
        System.out.println("Sending to userId: " + message.getReceiver());

    }
}
