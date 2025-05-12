package com.demo.chatApp.service.chat;

import com.demo.chatApp.model.ChatMessage;
import com.demo.chatApp.rabbitmq.MessageProducer;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ChatMessageService {

    private final MessageProducer messageProducer;

    public void handleOutgoingMessage(ChatMessage chatMessage) {
        messageProducer.sendMessage(chatMessage);
    }
}

