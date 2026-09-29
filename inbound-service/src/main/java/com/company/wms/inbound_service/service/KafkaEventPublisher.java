package com.company.wms.inbound_service.service;

import com.company.wms.inbound_service.domain.OutboxEvent;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Service
public class KafkaEventPublisher {

    private static final String GOODS_RECEIVED_TOPIC =
            "goods-received";

    private final KafkaTemplate<String, String> kafkaTemplate;

    public KafkaEventPublisher(
            KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public CompletableFuture<?> publish(OutboxEvent event) {

        return kafkaTemplate.send(
                GOODS_RECEIVED_TOPIC,
                event.getEntityId(),
                event.getPayload()
        );
    }
}