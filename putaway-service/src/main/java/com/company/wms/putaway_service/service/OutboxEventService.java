package com.company.wms.putaway_service.service;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.company.wms.putaway_service.domain.OutboxEvent;
import com.company.wms.putaway_service.domain.OutboxEventStatus;
import com.company.wms.putaway_service.domain.PutAwayTask;
import com.company.wms.putaway_service.dto.PutAwayCompletedEvent;
import com.company.wms.putaway_service.repository.OutboxEventRepository;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class OutboxEventService {

    private final OutboxEventRepository outboxEventRepository;
    private final ObjectMapper objectMapper;

    public OutboxEventService(
            OutboxEventRepository outboxEventRepository,
            ObjectMapper objectMapper) {

        this.outboxEventRepository =
                outboxEventRepository;

        this.objectMapper = objectMapper;
    }

    public void createPutAwayCompletedEvent(
            PutAwayTask task) {

        try {

            UUID eventId = UUID.randomUUID();

            LocalDateTime now =
                    LocalDateTime.now();

            PutAwayCompletedEvent event =
                    new PutAwayCompletedEvent();

            event.setEventId(eventId);
            event.setWarehouseId(task.getWarehouseId());
            event.setEventType("PutAwayCompleted");
            event.setEventVersion(1);
            event.setOccurredAt(now);
            event.setSource("putaway-service");
            event.setCorrelationId(
                    UUID.randomUUID().toString());

            event.setPutAwayTaskId(task.getId());
            event.setTaskNumber(task.getTaskNumber());
            event.setGoodsReceiptLineId(
                    task.getGoodsReceiptLineId());
            event.setSkuId(task.getSkuId());
            event.setQuantity(task.getQuantity());
            event.setTargetBinId(task.getTargetBinId());

            String payload =
                    objectMapper.writeValueAsString(event);

            OutboxEvent outboxEvent =
                    new OutboxEvent();

            outboxEvent.setId(UUID.randomUUID());
            outboxEvent.setEventId(eventId);
            outboxEvent.setEventType(
                    "PutAwayCompleted");
            outboxEvent.setEventVersion(1);
            outboxEvent.setOccurredAt(now);
            outboxEvent.setSource(
                    "putaway-service");
            outboxEvent.setCorrelationId(
                    event.getCorrelationId());
            outboxEvent.setEntityId(
                    task.getId().toString());
            outboxEvent.setPayload(payload);
            outboxEvent.setStatus(
                    OutboxEventStatus.PENDING);
            outboxEvent.setRetryCount(0);
            outboxEvent.setCreatedAt(now);

            outboxEventRepository.save(
                    outboxEvent);

        } catch (Exception exception) {

            throw new RuntimeException(
                    "Failed to create PutAwayCompleted outbox event",
                    exception);
        }
    }
}