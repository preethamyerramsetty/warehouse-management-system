
package com.company.wms.inbound_service.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.company.wms.inbound_service.domain.GoodsReceipt;
import com.company.wms.inbound_service.domain.GoodsReceiptLine;
import com.company.wms.inbound_service.domain.OutboxEvent;
import com.company.wms.inbound_service.domain.OutboxEventStatus;
import com.company.wms.inbound_service.dto.GoodsReceivedEvent;
import com.company.wms.inbound_service.repository.OutboxEventRepository;
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

    public void createGoodsReceivedEvent(
            GoodsReceipt goodsReceipt,
            List<GoodsReceiptLine> lines,
            String correlationId) {

        UUID eventId = UUID.randomUUID();
        LocalDateTime now = LocalDateTime.now();

        String effectiveCorrelationId =
                correlationId != null && !correlationId.isBlank()
                        ? correlationId
                        : UUID.randomUUID().toString();

        GoodsReceivedEvent event = new GoodsReceivedEvent();
        event.setEventId(eventId);
        event.setEventType("GoodsReceived");
        event.setEventVersion(1);
        event.setOccurredAt(now);
        event.setSource("inbound-service");
        event.setCorrelationId(effectiveCorrelationId);
        event.setEntityId(goodsReceipt.getId().toString());

        GoodsReceivedEvent.Payload payload =
                new GoodsReceivedEvent.Payload();

        payload.setGoodsReceiptId(goodsReceipt.getId());
        payload.setWarehouseId(goodsReceipt.getWarehouseId());
        payload.setReceiptNumber(goodsReceipt.getReceiptNumber());

        List<GoodsReceivedEvent.GoodsReceivedLineEvent> eventLines =
                lines.stream()
                        .map(this::toEventLine)
                        .toList();

        payload.setLines(eventLines);
        event.setPayload(payload);

        String serializedEvent;

        try {
            serializedEvent = objectMapper.writeValueAsString(event);
        } catch (JsonProcessingException exception) {
            throw new IllegalStateException(
                    "Failed to serialize GoodsReceived event",
                    exception);
        }

        OutboxEvent outboxEvent = new OutboxEvent();
        outboxEvent.setEventId(eventId);
        outboxEvent.setEventType("GoodsReceived");
        outboxEvent.setEventVersion(1);
        outboxEvent.setOccurredAt(now);
        outboxEvent.setSource("inbound-service");
        outboxEvent.setCorrelationId(effectiveCorrelationId);
        outboxEvent.setEntityId(goodsReceipt.getId().toString());
        outboxEvent.setPayload(serializedEvent);
        outboxEvent.setStatus(OutboxEventStatus.PENDING);
        outboxEvent.setRetryCount(0);
        outboxEvent.setCreatedAt(now);
        outboxEvent.setNextAttemptAt(now);

        outboxEventRepository.save(outboxEvent);
    }

    private GoodsReceivedEvent.GoodsReceivedLineEvent toEventLine(
            GoodsReceiptLine line) {

        GoodsReceivedEvent.GoodsReceivedLineEvent eventLine =
                new GoodsReceivedEvent.GoodsReceivedLineEvent();

        eventLine.setGoodsReceiptLineId(line.getId());
        eventLine.setSkuId(line.getSkuId());
        eventLine.setQuantity(line.getReceivedQuantity());
        eventLine.setUom(line.getUom());
        eventLine.setBatchNumber(line.getBatchNumber());
        eventLine.setSerialNumber(line.getSerialNumber());

        return eventLine;
    }
}