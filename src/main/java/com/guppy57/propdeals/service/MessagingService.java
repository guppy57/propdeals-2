package com.guppy57.propdeals.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.guppy57.propdeals.dto.QueueMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.sqs.SqsClient;
import software.amazon.awssdk.services.sqs.model.DeleteMessageRequest;
import software.amazon.awssdk.services.sqs.model.Message;
import software.amazon.awssdk.services.sqs.model.ReceiveMessageRequest;
import software.amazon.awssdk.services.sqs.model.ReceiveMessageResponse;
import software.amazon.awssdk.services.sqs.model.SendMessageRequest;

import java.util.List;

@Service
public class MessagingService {
    private static final Logger logger = LoggerFactory.getLogger(MessagingService.class);

    private final SqsClient sqsClient;
    private final ObjectMapper objectMapper;
    private final CalculationService calculationService;

    @Value("${aws.sqs.queue-url}")
    private String queueUrl;

    public MessagingService(SqsClient sqsClient, ObjectMapper objectMapper, CalculationService calculationService) {
        this.sqsClient = sqsClient;
        this.objectMapper = objectMapper;
        this.calculationService = calculationService;
    }

    public void publish(Object payload) {
        try {
            String body = objectMapper.writeValueAsString(payload);
            logger.info("Publishing message to SQS: {}", body);
            sqsClient.sendMessage(SendMessageRequest.builder()
                    .queueUrl(queueUrl)
                    .messageBody(body)
                    .build());
            logger.info("Message published successfully");
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to serialize message for SQS", e);
        }
    }

    @Scheduled(fixedDelay = 1000)
    public void poll() {
        try {
            ReceiveMessageResponse response = sqsClient.receiveMessage(
                    ReceiveMessageRequest.builder()
                            .queueUrl(queueUrl)
                            .maxNumberOfMessages(10)
                            .waitTimeSeconds(20)
                            .build()
            );
            List<Message> messages = response.messages();
            if (!messages.isEmpty()) {
                logger.info("Received {} message(s) from SQS", messages.size());
            }
            for (Message message : messages) {
                try {
                    QueueMessage queueMessage = objectMapper.readValue(message.body(), QueueMessage.class);
                    handle(queueMessage);
                    sqsClient.deleteMessage(DeleteMessageRequest.builder()
                            .queueUrl(queueUrl)
                            .receiptHandle(message.receiptHandle())
                            .build());
                } catch (Exception e) {
                    logger.error("Failed to process SQS message; leaving for requeue: {}", message.body(), e);
                }
            }
        } catch (Exception e) {
            logger.warn("SQS poll failed (will retry): {}", e.getMessage());
        }
    }

    private void handle(QueueMessage message) {
        logger.info("Handling queue message: {}", message);
        switch (message.processType()) {
            case NEW_ANALYSIS -> calculationService.processNewAnalysis(message);
            case REPROCESS    -> calculationService.processReanalysis(message);
        }
    }
}
