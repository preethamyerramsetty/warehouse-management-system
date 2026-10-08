package com.company.wms.inventory_service.service;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.company.wms.inventory_service.domain.InventoryBalance;
import com.company.wms.inventory_service.domain.OutboxEvent;
import com.company.wms.inventory_service.domain.OutboxEventStatus;
import com.company.wms.inventory_service.dto.InventoryUpdatedEvent;
import com.company.wms.inventory_service.dto.PutAwayCompletedEvent;
import com.company.wms.inventory_service.repository.OutboxEventRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class OutboxEventService {

    private final OutboxEventRepository outboxEventRepository;
    private final ObjectMapper objectMapper;

    public OutboxEventService(
            OutboxEventRepository outboxEventRepository,
            ObjectMapper objectMapper) {

        this.outboxEventRepository = outboxEventRepository;
        this.objectMapper = objectMapper;
    }

    public void createInventoryUpdatedEvent(
            PutAwayCompletedEvent sourceEvent,
            InventoryBalance balance) {

        try {
            InventoryUpdatedEvent event =
                    new InventoryUpdatedEvent();

            event.setEventId(UUID.randomUUID());
            event.setEventType("InventoryUpdated");
            event.setEventVersion(1);
            event.setOccurredAt(LocalDateTime.now());
            event.setSource("inventory-service");
            event.setCorrelationId(sourceEvent.getCorrelationId());

            event.setSkuId(balance.getSkuId());
            event.setWarehouseId(balance.getWarehouseId());
            event.setBinId(balance.getBinId());

            event.setQuantityChanged(sourceEvent.getQuantity());
            event.setAvailableQuantity(
                    balance.getAvailableQuantity()
            );

            event.setMovementType("PUTAWAY");
            event.setReferenceId(
                    sourceEvent.getPutAwayTaskId()
            );

            OutboxEvent outboxEvent = new OutboxEvent();

            outboxEvent.setId(UUID.randomUUID());
            outboxEvent.setEventId(event.getEventId());
            outboxEvent.setEventType("InventoryUpdated");
            outboxEvent.setEventVersion(1);
            outboxEvent.setOccurredAt(event.getOccurredAt());
            outboxEvent.setSource("inventory-service");
            outboxEvent.setCorrelationId(event.getCorrelationId());
            outboxEvent.setEntityId(
                    balance.getSkuId().toString()
            );

            outboxEvent.setPayload(
                    objectMapper.writeValueAsString(event)
            );

            outboxEvent.setStatus(OutboxEventStatus.PENDING);
            outboxEvent.setRetryCount(0);
            outboxEvent.setCreatedAt(LocalDateTime.now());
            outboxEvent.setNextAttemptAt(LocalDateTime.now());

            outboxEventRepository.save(outboxEvent);

        } catch (JsonProcessingException e) {
            throw new IllegalStateException(
                    "Failed to create InventoryUpdated event",
                    e
            );
        }
    }
}