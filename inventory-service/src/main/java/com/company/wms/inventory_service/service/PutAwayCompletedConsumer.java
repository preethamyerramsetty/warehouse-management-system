
package com.company.wms.inventory_service.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import com.company.wms.inventory_service.dto.PutAwayCompletedEvent;
import com.fasterxml.jackson.databind.ObjectMapper;

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
            PutAwayCompletedEvent event =
                    objectMapper.readValue(
                            message,
                            PutAwayCompletedEvent.class
                    );

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
