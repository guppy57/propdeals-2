package com.guppy57.propdeals.service;

import com.guppy57.propdeals.config.RabbitMQConfig;
import com.guppy57.propdeals.dto.QueueMessage;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
public class MessagingService {

    private final RabbitTemplate rabbitTemplate;
    private final CalculationService calculationService;

    public MessagingService(RabbitTemplate rabbitTemplate, CalculationService calculationService) {
        this.rabbitTemplate = rabbitTemplate;
        this.calculationService = calculationService;
    }

    public void publish(Object payload) {
        rabbitTemplate.convertAndSend(
                RabbitMQConfig.EXCHANGE_NAME,
                RabbitMQConfig.ROUTING_KEY,
                payload
        );
    }

    @RabbitListener(queues = RabbitMQConfig.QUEUE_NAME)
    public void handle(QueueMessage message) {
        switch (message.processType()) {
            case NEW_ANALYSIS -> calculationService.processNewAnalysis(message);
            case REPROCESS    -> calculationService.processReanalysis(message);
        }
    }
}