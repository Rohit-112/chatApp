package com.demo.chatApp.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableRabbit
public class RabbitMQConfig {

    public static final String CHAT_QUEUE = "chat.queue";
    public static final String CHAT_EXCHANGE = "chat.exchange";
    public static final String CHAT_ROUTING_KEY = "chat.routingKey";


    @Bean
    public TopicExchange chatExchange() {
        return new TopicExchange(CHAT_EXCHANGE);
    }

    @Bean
    public Jackson2JsonMessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setMessageConverter(messageConverter());
        return template;
    }

    @Bean
    public Declarables userBindings(AmqpAdmin amqpAdmin) {
        String[] username = {};

        Declarables declarables = new Declarables();

        for (String user : username) {
            String queueName = "chat.queue." + user;
            String routingKey = "chat.user." + user;

            Queue queue = new Queue(queueName, true);
            Binding binding = BindingBuilder.bind(queue)
                    .to(chatExchange())
                    .with(routingKey);

            declarables.getDeclarables().add(queue);
            declarables.getDeclarables().add(binding);
        }
        return declarables;
    }

    public static String getQueueName(String username) {
        return "chat.queue." + username;
    }

    public static String getRoutingKey(String username) {
        return "chat.user." + username;
    }


    public void setupQueueForUser(String username, AmqpAdmin amqpAdmin) {

        String queueName = getQueueName(username);
        String routingKey = getRoutingKey(username);

        Queue queue = new Queue(queueName, true);
        Binding binding = BindingBuilder.bind(queue)
                .to(chatExchange())
                .with(routingKey);

        amqpAdmin.declareQueue(queue);
        amqpAdmin.declareBinding(binding);
    }
}

