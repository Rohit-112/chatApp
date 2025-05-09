package com.demo.chatApp.controller;

import com.demo.chatApp.model.ChatMessage;
import com.demo.chatApp.model.chat.Message;
import com.demo.chatApp.service.chat.ChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;

import org.springframework.messaging.simp.SimpMessagingTemplate;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;

import java.security.Principal;

@Controller
public class ChatController {

    private final SimpMessagingTemplate messagingTemplate;
    private final ChatService chatService;

    @Autowired
    public ChatController(SimpMessagingTemplate messagingTemplate, ChatService chatService) {
        this.messagingTemplate = messagingTemplate;
        this.chatService = chatService;
    }

    @MessageMapping("/chat/{receiver}")
    public ChatMessage sendMessage(@DestinationVariable String receiver,
                                   @RequestBody ChatMessage message,
                                   Principal principal) {

        String senderName = principal.getName();
        System.out.println("sending message from: " + principal.getName());
        System.out.println("sending message to: " + receiver);
        Message savedMessage = chatService.saveMessage(senderName, receiver, message.getMessage());
        message.setSenderName(senderName);
        messagingTemplate.convertAndSendToUser(receiver, "/queue/messages", message);
        return message;
    }
}
