package com.company.wms.inventory_service.service;

import com.company.wms.inventory_service.dto.PutAwayCompletedEvent;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class PutAwayCompletedConsumer {

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

        try {

            PutAwayCompletedEvent event =
                    objectMapper.readValue(
                            message,
                            PutAwayCompletedEvent.class
                    );

            inventoryProcessingService
                    .processPutAwayCompleted(event);

        } catch (Exception e) {

            throw new RuntimeException(
                    "Failed to process PutAwayCompleted event",
                    e
            );
        }
    }
}