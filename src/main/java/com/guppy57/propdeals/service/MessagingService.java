package com.guppy57.propdeals.service;

import com.guppy57.propdeals.config.RabbitMQConfig;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
public class MessagingService {

    private final RabbitTemplate rabbitTemplate;

    public MessagingService(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void publish(Object payload) {
        rabbitTemplate.convertAndSend(
                RabbitMQConfig.EXCHANGE_NAME,
                RabbitMQConfig.ROUTING_KEY,
                payload
        );
    }

    @RabbitListener(queues = RabbitMQConfig.QUEUE_NAME)
    public void handle(Object message) {
        // TODO: dispatch to appropriate handler based on message type
    }
}