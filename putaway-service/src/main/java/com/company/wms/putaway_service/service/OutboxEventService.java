
package com.company.wms.putaway_service.service;

import java.time.LocalDateTime;
import java.util.UUID;

import org.slf4j.MDC;
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

        this.outboxEventRepository = outboxEventRepository;
        this.objectMapper = objectMapper;
    }

    public void createPutAwayCompletedEvent(PutAwayTask task) {

        try {
            UUID eventId = UUID.randomUUID();
            LocalDateTime now = LocalDateTime.now();

            String correlationId = MDC.get("correlationId");

            if (correlationId == null || correlationId.isBlank()) {
                correlationId = UUID.randomUUID().toString();
            }

            PutAwayCompletedEvent.Payload payload =
                    new PutAwayCompletedEvent.Payload();

            payload.setPutAwayTaskId(task.getId());
            payload.setTaskNumber(task.getTaskNumber());
            payload.setGoodsReceiptLineId(
                    task.getGoodsReceiptLineId());
            payload.setSkuId(task.getSkuId());
            payload.setQuantity(task.getQuantity());
            payload.setWarehouseId(task.getWarehouseId());
            payload.setTargetBinId(task.getTargetBinId());

            PutAwayCompletedEvent event =
                    new PutAwayCompletedEvent();

            event.setEventId(eventId);
            event.setEventType("PutAwayCompleted");
            event.setEventVersion(1);
            event.setOccurredAt(now);
            event.setSource("putaway-service");
            event.setCorrelationId(correlationId);
            event.setEntityId(task.getId().toString());
            event.setPayload(payload);

            String serializedEvent =
                    objectMapper.writeValueAsString(event);

            OutboxEvent outboxEvent = new OutboxEvent();

            outboxEvent.setId(UUID.randomUUID());
            outboxEvent.setEventId(eventId);
            outboxEvent.setEventType("PutAwayCompleted");
            outboxEvent.setEventVersion(1);
            outboxEvent.setOccurredAt(now);
            outboxEvent.setSource("putaway-service");
            outboxEvent.setCorrelationId(correlationId);
            outboxEvent.setEntityId(task.getId().toString());
            outboxEvent.setPayload(serializedEvent);
            outboxEvent.setStatus(OutboxEventStatus.PENDING);
            outboxEvent.setRetryCount(0);
            outboxEvent.setCreatedAt(now);
            outboxEvent.setNextAttemptAt(now);

            outboxEventRepository.save(outboxEvent);

        } catch (Exception exception) {
            throw new RuntimeException(
                    "Failed to create PutAwayCompleted outbox event",
                    exception);
        }
    }
}
