package com.demo.chatApp.service.chat;

import com.demo.chatApp.config.RabbitMQConfig;
import com.demo.chatApp.model.ChatMessage;
import com.demo.chatApp.rabbitmq.MessageProducer;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ChatMessageService {

   /* private final MessageProducer messageProducer;

    public void handleOutgoingMessage(ChatMessage chatMessage) {
        messageProducer.sendMessage(chatMessage);
    }*/

    @Autowired
    private RabbitTemplate rabbitTemplate;

    public void handleOutgoingMessage(ChatMessage message) {
        String routingKey = RabbitMQConfig.getRoutingKey(message.getReceiverName());
        rabbitTemplate.convertAndSend(RabbitMQConfig.CHAT_EXCHANGE, routingKey, message);
    }
}

