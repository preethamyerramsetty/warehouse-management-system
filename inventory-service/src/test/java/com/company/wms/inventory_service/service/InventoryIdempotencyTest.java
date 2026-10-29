package com.company.wms.inventory_service.service;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import java.util.UUID;

import com.company.wms.inventory_service.dto.PutAwayCompletedEvent;
import com.company.wms.inventory_service.repository.InventoryBalanceRepository;
import com.company.wms.inventory_service.repository.InventoryMovementRepository;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class InventoryIdempotencyTest {

    @Mock
    private InventoryBalanceRepository inventoryBalanceRepository;

    @Mock
    private InventoryMovementRepository inventoryMovementRepository;

    @Mock
    private OutboxEventService outboxEventService;

    @InjectMocks
    private InventoryProcessingService inventoryProcessingService;

    @Test
    void processPutAwayCompleted_whenEventWasAlreadyProcessed_doesNotUpdateInventory() {
        UUID eventId = UUID.randomUUID();

        PutAwayCompletedEvent event = new PutAwayCompletedEvent();
        event.setEventId(eventId);

        when(inventoryMovementRepository.existsByEventId(eventId))
                .thenReturn(true);

        assertDoesNotThrow(
                () -> inventoryProcessingService.processPutAwayCompleted(event)
        );

        verify(inventoryMovementRepository).existsByEventId(eventId);
        verifyNoInteractions(inventoryBalanceRepository);
        verify(inventoryMovementRepository, never()).save(
                org.mockito.ArgumentMatchers.any()
        );
        verifyNoInteractions(outboxEventService);
    }
}
