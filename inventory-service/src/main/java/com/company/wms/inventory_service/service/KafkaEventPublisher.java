package com.company.wms.inventory_service.service;

import java.util.concurrent.CompletableFuture;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.company.wms.inventory_service.domain.OutboxEvent;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

@Service
public class KafkaEventPublisher {

    private static final String INVENTORY_UPDATED_TOPIC =
            "inventory-updated";

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;

    public KafkaEventPublisher(
            KafkaTemplate<String, String> kafkaTemplate,
            ObjectMapper objectMapper) {
        this.kafkaTemplate = kafkaTemplate;
        this.objectMapper = objectMapper;
    }

    public CompletableFuture<?> publish(OutboxEvent event) {
        try {
            JsonNode payloadNode = objectMapper.readTree(event.getPayload());

            if (!(payloadNode instanceof ObjectNode)) {
                throw new IllegalArgumentException(
                        "Event payload must be a JSON object");
            }

            ObjectNode payload = (ObjectNode) payloadNode;

            payload.remove(java.util.List.of(
                    "eventId",
                    "eventType",
                    "eventVersion",
                    "occurredAt",
                    "source",
                    "correlationId"
            ));

            ObjectNode envelope = objectMapper.createObjectNode();
            envelope.put("eventId", event.getEventId().toString());
            envelope.put("eventType", event.getEventType());
            envelope.put("eventVersion", event.getEventVersion());
            envelope.put("occurredAt", event.getOccurredAt().toString());
            envelope.put("source", event.getSource());
            envelope.put("correlationId", event.getCorrelationId());
            envelope.put("entityId", event.getEntityId());
            envelope.set("payload", payload);

            String message = objectMapper.writeValueAsString(envelope);

            return kafkaTemplate.send(
                    INVENTORY_UPDATED_TOPIC,
                    event.getEntityId(),
                    message
            );

        } catch (JsonProcessingException exception) {
            throw new IllegalStateException(
                    "Failed to create InventoryUpdated event envelope",
                    exception
            );
        }
    }
}
