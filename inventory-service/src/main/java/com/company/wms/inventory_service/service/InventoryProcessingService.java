
package com.company.wms.inventory_service.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.company.wms.inventory_service.domain.InventoryBalance;
import com.company.wms.inventory_service.domain.InventoryMovement;
import com.company.wms.inventory_service.dto.PutAwayCompletedEvent;
import com.company.wms.inventory_service.repository.InventoryBalanceRepository;
import com.company.wms.inventory_service.repository.InventoryMovementRepository;

import jakarta.transaction.Transactional;

@Service
public class InventoryProcessingService {

    private final InventoryBalanceRepository inventoryBalanceRepository;
    private final InventoryMovementRepository inventoryMovementRepository;
    private final OutboxEventService outboxEventService;

    public InventoryProcessingService(
            InventoryBalanceRepository inventoryBalanceRepository,
            InventoryMovementRepository inventoryMovementRepository,
            OutboxEventService outboxEventService) {

        this.inventoryBalanceRepository = inventoryBalanceRepository;
        this.inventoryMovementRepository = inventoryMovementRepository;
        this.outboxEventService = outboxEventService;
    }

    @Transactional
    public void processPutAwayCompleted(PutAwayCompletedEvent event) {

        if (event == null
                || event.getEventId() == null
                || event.getPayload() == null) {
            throw new IllegalArgumentException(
                    "PutAwayCompleted event, eventId and payload are required");
        }

        PutAwayCompletedEvent.Payload payload = event.getPayload();

        // Avoid applying the same event more than once.
        if (inventoryMovementRepository.existsByEventId(event.getEventId())) {
            return;
        }

        InventoryBalance balance =
                inventoryBalanceRepository
                        .findBySkuIdAndWarehouseIdAndBinId(
                                payload.getSkuId(),
                                payload.getWarehouseId(),
                                payload.getTargetBinId())
                        .orElseGet(() -> createNewBalance(payload));

        BigDecimal currentQuantity = balance.getAvailableQuantity();

        balance.setAvailableQuantity(
                currentQuantity.add(payload.getQuantity()));

        balance.setUpdatedAt(LocalDateTime.now());

        inventoryBalanceRepository.save(balance);

        // Record the stock movement for this event.
        InventoryMovement movement = new InventoryMovement();

        movement.setId(UUID.randomUUID());
        movement.setSkuId(payload.getSkuId());
        movement.setWarehouseId(payload.getWarehouseId());
        movement.setBinId(payload.getTargetBinId());
        movement.setMovementType("PUTAWAY");
        movement.setQuantity(payload.getQuantity());
        movement.setReferenceType("PUTAWAY_TASK");
        movement.setReferenceId(payload.getPutAwayTaskId().toString());
        movement.setEventId(event.getEventId());
        movement.setCreatedAt(LocalDateTime.now());

        inventoryMovementRepository.save(movement);

        // Publish the inventory change using the existing outbox flow.
        outboxEventService.createInventoryUpdatedEvent(event, balance);
    }

    private InventoryBalance createNewBalance(
            PutAwayCompletedEvent.Payload payload) {

        InventoryBalance balance = new InventoryBalance();

        balance.setId(UUID.randomUUID());
        balance.setSkuId(payload.getSkuId());
        balance.setWarehouseId(payload.getWarehouseId());
        balance.setBinId(payload.getTargetBinId());
        balance.setAvailableQuantity(BigDecimal.ZERO);
        balance.setReservedQuantity(BigDecimal.ZERO);
        balance.setUpdatedAt(LocalDateTime.now());

        return balance;
    }
}
