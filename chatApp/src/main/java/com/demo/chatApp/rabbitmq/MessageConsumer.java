package com.demo.chatApp.rabbitmq;

import com.demo.chatApp.model.ChatMessage;
import com.demo.chatApp.service.chat.ChatService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class MessageConsumer {

    private final ChatService chatService;
    private final SimpMessagingTemplate messagingTemplate;
    @Autowired
    private RabbitTemplate rabbitTemplate;

    @RabbitListener(queues = "chat.queue")
    public void receiveMessage(ChatMessage chatMessage) {
        try {
            log.info("Received message from queue: {}", chatMessage);

            chatService.saveMessage(
                    chatMessage.getSenderName(),
                    chatMessage.getReceiverName(),
                    chatMessage.getMessage()
            );

//            messagingTemplate.convertAndSend("/queue/chat.user." + chatMessage.getReceiverName(), chatMessage);

            log.info("Message sent to WebSocket for user: {}", chatMessage.getReceiverName());

        } catch (Exception e) {
            log.error("Failed to process message: {}", chatMessage, e);
        }
    }
}

