package com.company.wms.putaway_service.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.company.wms.putaway_service.domain.ProcessedEvent;
import com.company.wms.putaway_service.domain.PutAwayReceiptLine;
import com.company.wms.putaway_service.dto.GoodsReceivedEvent;
import com.company.wms.putaway_service.repository.ProcessedEventRepository;
import com.company.wms.putaway_service.repository.PutAwayReceiptLineRepository;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class GoodsReceivedConsumer {

    private final ObjectMapper objectMapper;
    private final ProcessedEventRepository processedEventRepository;
    private final PutAwayReceiptLineRepository putAwayReceiptLineRepository;

    public GoodsReceivedConsumer(
            ObjectMapper objectMapper,
            ProcessedEventRepository processedEventRepository,
            PutAwayReceiptLineRepository putAwayReceiptLineRepository) {

        this.objectMapper = objectMapper;
        this.processedEventRepository = processedEventRepository;
        this.putAwayReceiptLineRepository =
                putAwayReceiptLineRepository;
    }

    @KafkaListener(
            topics = "goods-received",
            groupId = "putaway-service-test"
    )
    @Transactional
    public void consume(String message) {

        try {

            GoodsReceivedEvent event =
                    objectMapper.readValue(
                            message,
                            GoodsReceivedEvent.class);

            if (processedEventRepository
                    .existsByEventId(event.getEventId())) {

                System.out.println(
                        "GoodsReceived event already processed: "
                                + event.getEventId());

                return;
            }

            for (GoodsReceivedEvent.GoodsReceivedLineEvent line
                    : event.getLines()) {

                PutAwayReceiptLine receiptLine =
                        new PutAwayReceiptLine();

                receiptLine.setId(UUID.randomUUID());

                receiptLine.setGoodsReceiptLineId(
                        line.getGoodsReceiptLineId());

                receiptLine.setSkuId(
                        line.getSkuId());

                BigDecimal receivedQuantity =
                        line.getQuantity();

                receiptLine.setReceivedQuantity(
                        receivedQuantity);

                receiptLine.setPutawayQuantity(
                        BigDecimal.ZERO);

                receiptLine.setRemainingQuantity(
                        receivedQuantity);

                receiptLine.setCreatedAt(
                        LocalDateTime.now());

                receiptLine.setUpdatedAt(
                        LocalDateTime.now());

                putAwayReceiptLineRepository.save(
                        receiptLine);
            }

            ProcessedEvent processedEvent =
                    new ProcessedEvent();

            processedEvent.setId(UUID.randomUUID());

            processedEvent.setEventId(
                    event.getEventId());

            processedEvent.setEventType(
                    event.getEventType());

            processedEvent.setProcessedAt(
                    LocalDateTime.now());

            processedEventRepository.save(
                    processedEvent);

            System.out.println(
                    "Processed GoodsReceived event: "
                            + event.getEventId());

        } catch (Exception exception) {

            System.err.println(
                    "Failed to process GoodsReceived event: "
                            + exception.getMessage());

            throw new RuntimeException(
                    "Failed to process GoodsReceived event",
                    exception);
        }
    }
}