
package com.company.wms.inventory_service.service;

import java.time.LocalDateTime;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import com.company.wms.inventory_service.dto.PutAwayCompletedEvent;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.LocalDateTime;
import java.util.UUID;

import com.fasterxml.jackson.databind.JsonNode;

@Component
public class PutAwayCompletedConsumer {

    private static final Logger log =
            LoggerFactory.getLogger(PutAwayCompletedConsumer.class);

    private final ObjectMapper objectMapper;
    private final InventoryProcessingService inventoryProcessingService;

    public PutAwayCompletedConsumer(
            ObjectMapper objectMapper,
            InventoryProcessingService inventoryProcessingService) {

        this.objectMapper = objectMapper;
        this.inventoryProcessingService = inventoryProcessingService;
    }

    @KafkaListener(
            topics = "putaway-completed",
            groupId = "inventory-service"
    )
    public void consume(String message) {

        String correlationId = null;

        try {
            
                JsonNode envelope = objectMapper.readTree(message);

                if (envelope.path("payload").isMissingNode()
                        || envelope.path("eventId").isMissingNode()
                        || envelope.path("entityId").isMissingNode()) {
                    throw new IllegalArgumentException(
                            "Invalid PutAwayCompleted event envelope");
                }

                if (!"PutAwayCompleted".equals(
                        envelope.path("eventType").asText())) {
                    throw new IllegalArgumentException(
                            "Unexpected event type");
                }

                PutAwayCompletedEvent event = objectMapper.treeToValue(
                        envelope.get("payload"),
                        PutAwayCompletedEvent.class
                );

                event.setEventId(UUID.fromString(
                        envelope.path("eventId").asText()));
                event.setEventType(envelope.path("eventType").asText());
                event.setEventVersion(envelope.path("eventVersion").asInt());
                event.setOccurredAt(LocalDateTime.parse(
                        envelope.path("occurredAt").asText()));
                event.setSource(envelope.path("source").asText());
                event.setCorrelationId(
                        envelope.path("correlationId").asText());

                correlationId = event.getCorrelationId();


            if (correlationId != null && !correlationId.isBlank()) {
                MDC.put("correlationId", correlationId);
            }

            log.info(
                    "Received PutAwayCompleted event: {}",
                    event.getEventId()
            );

            inventoryProcessingService.processPutAwayCompleted(event);

        } catch (Exception e) {
            log.error("Failed to process PutAwayCompleted event", e);

            throw new RuntimeException(
                    "Failed to process PutAwayCompleted event",
                    e
            );

        } finally {
            MDC.remove("correlationId");
        }
    }
}
