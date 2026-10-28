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
            PutAwayCompletedEvent.Payload sourcePayload =
                    sourceEvent.getPayload();

            UUID eventId = UUID.randomUUID();
            LocalDateTime now = LocalDateTime.now();
            String entityId = balance.getSkuId().toString();

            InventoryUpdatedEvent.Payload payload =
                    new InventoryUpdatedEvent.Payload();

            payload.setSkuId(balance.getSkuId());
            payload.setWarehouseId(balance.getWarehouseId());
            payload.setBinId(balance.getBinId());
            payload.setQuantityChanged(sourcePayload.getQuantity());
            payload.setAvailableQuantity(
                    balance.getAvailableQuantity());
            payload.setMovementType("PUTAWAY");
            payload.setReferenceId(
                    sourcePayload.getPutAwayTaskId());

            InventoryUpdatedEvent event =
                    new InventoryUpdatedEvent();

            event.setEventId(eventId);
            event.setEventType("InventoryUpdated");
            event.setEventVersion(1);
            event.setOccurredAt(now);
            event.setSource("inventory-service");
            event.setCorrelationId(sourceEvent.getCorrelationId());
            event.setEntityId(entityId);
            event.setPayload(payload);

            OutboxEvent outboxEvent = new OutboxEvent();

            outboxEvent.setId(UUID.randomUUID());
            outboxEvent.setEventId(eventId);
            outboxEvent.setEventType("InventoryUpdated");
            outboxEvent.setEventVersion(1);
            outboxEvent.setOccurredAt(now);
            outboxEvent.setSource("inventory-service");
            outboxEvent.setCorrelationId(event.getCorrelationId());
            outboxEvent.setEntityId(entityId);
            outboxEvent.setPayload(
                    objectMapper.writeValueAsString(event));

            outboxEvent.setStatus(OutboxEventStatus.PENDING);
            outboxEvent.setRetryCount(0);
            outboxEvent.setCreatedAt(now);
            outboxEvent.setNextAttemptAt(now);

            outboxEventRepository.save(outboxEvent);

        } catch (JsonProcessingException exception) {
            throw new IllegalStateException(
                    "Failed to create InventoryUpdated event",
                    exception);
        }
    }
}
