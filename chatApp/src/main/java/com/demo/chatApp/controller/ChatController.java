package com.demo.chatApp.controller;

import com.demo.chatApp.model.ChatMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;

import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;

import java.security.Principal;

@Controller
public class ChatController {

    private final SimpMessagingTemplate messagingTemplate;

    @Autowired
    public ChatController(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    @MessageMapping("/chat/{receiver}")
    public ChatMessage sendMessage(@DestinationVariable String receiver,
                                   @RequestBody ChatMessage message,
                                   Principal principal) {

        System.out.println("sending message from: " + principal.getName());
        System.out.println("sending message to: " + receiver);
        message.setSenderName(principal.getName());
        messagingTemplate.convertAndSendToUser(receiver, "/queue/messages", message);
        return message;
    }
}
