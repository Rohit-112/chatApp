package com.demo.chatApp.rabbitmq;

import com.demo.chatApp.model.ChatMessage;
import com.demo.chatApp.service.chat.ChatService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class MessageConsumer {

    private final ChatService chatService;

    @RabbitListener(queues = "#{T(com.demo.chatApp.config.RabbitMQConfig).CHAT_QUEUE}")
    public void receiveMessage(ChatMessage chatMessage) {
        try {
            log.info("Received message from queue: {}", chatMessage);

            chatService.saveMessage(
                    chatMessage.getSenderName(),
                    chatMessage.getReceiverName(),
                    chatMessage.getMessage()
            );

        } catch (Exception e) {
            log.error("Failed to process message: {}", chatMessage, e);
            // Optionally: send to dead-letter queue, retry, or log for audit
        }
    }
}

