package com.company.wms.putaway_service.service;

import java.util.concurrent.CompletableFuture;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.company.wms.putaway_service.domain.OutboxEvent;

@Service
public class KafkaEventPublisher {

    private static final String PUTAWAY_COMPLETED_TOPIC =
            "putaway-completed";

    private final KafkaTemplate<String, String> kafkaTemplate;

    public KafkaEventPublisher(
            KafkaTemplate<String, String> kafkaTemplate) {

        this.kafkaTemplate = kafkaTemplate;
    }

    public CompletableFuture<?> publish(
            OutboxEvent event) {

        return kafkaTemplate.send(
                PUTAWAY_COMPLETED_TOPIC,
                event.getEntityId(),
                event.getPayload());
    }
}