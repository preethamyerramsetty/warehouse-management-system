package com.company.wms.inventory_service.service;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import com.company.wms.inventory_service.domain.InventoryBalance;
import com.company.wms.inventory_service.domain.InventoryMovement;
import com.company.wms.inventory_service.dto.PutAwayCompletedEvent;
import com.company.wms.inventory_service.repository.InventoryBalanceRepository;
import com.company.wms.inventory_service.repository.InventoryMovementRepository;

class InventoryProcessingServiceTest {

    private InventoryBalanceRepository balanceRepository;
    private InventoryMovementRepository movementRepository;
    private OutboxEventService outboxEventService;
    private InventoryProcessingService processingService;

    @BeforeEach
    void setUp() {
        balanceRepository = mock(InventoryBalanceRepository.class);
        movementRepository = mock(InventoryMovementRepository.class);
        outboxEventService = mock(OutboxEventService.class);

        processingService = new InventoryProcessingService(
                balanceRepository,
                movementRepository,
                outboxEventService
        );
    }

    @Test
    void shouldUpdateInventoryAndRecordMovement() {
        UUID eventId = UUID.randomUUID();
        UUID skuId = UUID.randomUUID();
        UUID warehouseId = UUID.randomUUID();
        UUID binId = UUID.randomUUID();
        UUID taskId = UUID.randomUUID();

        PutAwayCompletedEvent event = createEvent(
                eventId, skuId, warehouseId, binId, taskId,
                new BigDecimal("10")
        );

        InventoryBalance existingBalance = new InventoryBalance();
        existingBalance.setId(UUID.randomUUID());
        existingBalance.setSkuId(skuId);
        existingBalance.setWarehouseId(warehouseId);
        existingBalance.setBinId(binId);
        existingBalance.setAvailableQuantity(new BigDecimal("20"));
        existingBalance.setReservedQuantity(BigDecimal.ZERO);

        when(movementRepository.existsByEventId(eventId))
                .thenReturn(false);

        when(balanceRepository.findBySkuIdAndWarehouseIdAndBinId(
                skuId, warehouseId, binId))
                .thenReturn(Optional.of(existingBalance));

        processingService.processPutAwayCompleted(event);

        assertEquals(
                new BigDecimal("30"),
                existingBalance.getAvailableQuantity()
        );

        verify(balanceRepository).save(existingBalance);

        verify(movementRepository).save(argThat(
                (InventoryMovement movement) ->
                        movement.getEventId().equals(eventId)
                        && movement.getSkuId().equals(skuId)
                        && movement.getWarehouseId().equals(warehouseId)
                        && movement.getBinId().equals(binId)
                        && movement.getQuantity().equals(new BigDecimal("10"))
                        && movement.getMovementType().equals("PUTAWAY")
        ));

        verify(outboxEventService).createInventoryUpdatedEvent(
                event, existingBalance
        );
    }

    @Test
    void shouldIgnoreDuplicatePutAwayEvent() {
        UUID eventId = UUID.randomUUID();

        PutAwayCompletedEvent event = createEvent(
                eventId,
                UUID.randomUUID(),
                UUID.randomUUID(),
                UUID.randomUUID(),
                UUID.randomUUID(),
                new BigDecimal("10")
        );

        when(movementRepository.existsByEventId(eventId))
                .thenReturn(true);

        processingService.processPutAwayCompleted(event);

        verify(movementRepository).existsByEventId(eventId);
        verifyNoInteractions(balanceRepository);
        verify(movementRepository, never()).save(any(InventoryMovement.class));
        verifyNoInteractions(outboxEventService);
    }

    private PutAwayCompletedEvent createEvent(
            UUID eventId,
            UUID skuId,
            UUID warehouseId,
            UUID binId,
            UUID taskId,
            BigDecimal quantity) {

        PutAwayCompletedEvent.Payload payload =
                new PutAwayCompletedEvent.Payload();

        payload.setPutAwayTaskId(taskId);
        payload.setTaskNumber("PA-1001");
        payload.setGoodsReceiptLineId(UUID.randomUUID());
        payload.setSkuId(skuId);
        payload.setQuantity(quantity);
        payload.setWarehouseId(warehouseId);
        payload.setTargetBinId(binId);

        PutAwayCompletedEvent event = new PutAwayCompletedEvent();

        event.setEventId(eventId);
        event.setEventType("PutAwayCompleted");
        event.setEventVersion(1);
        event.setSource("putaway-service");
        event.setCorrelationId("test-correlation-id");
        event.setEntityId(taskId.toString());
        event.setPayload(payload);

        return event;
    }
}
