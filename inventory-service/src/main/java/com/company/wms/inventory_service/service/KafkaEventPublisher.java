package com.company.wms.inventory_service.service;

import java.util.concurrent.CompletableFuture;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.company.wms.inventory_service.domain.OutboxEvent;

@Service
public class KafkaEventPublisher {

    private static final String INVENTORY_UPDATED_TOPIC =
            "inventory-updated";

    private final KafkaTemplate<String, String> kafkaTemplate;

    public KafkaEventPublisher(
            KafkaTemplate<String, String> kafkaTemplate) {

        this.kafkaTemplate = kafkaTemplate;
    }

    public CompletableFuture<?> publish(OutboxEvent event) {

        return kafkaTemplate.send(
                INVENTORY_UPDATED_TOPIC,
                event.getEntityId(),
                event.getPayload()
        );
    }
}