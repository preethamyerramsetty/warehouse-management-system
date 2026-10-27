
package com.company.wms.inventory_service.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.company.wms.inventory_service.domain.InventoryBalance;
import com.company.wms.inventory_service.domain.InventoryMovement;
import com.company.wms.inventory_service.dto.PutAwayCompletedEvent;
import com.company.wms.inventory_service.repository.InventoryBalanceRepository;
import com.company.wms.inventory_service.repository.InventoryMovementRepository;

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

        // Validate before accessing the database.
        validateEvent(event);

        // A previously processed event must not add stock again.
        if (inventoryMovementRepository.existsByEventId(event.getEventId())) {
            return;
        }

        LocalDateTime now = LocalDateTime.now();

        InventoryBalance balance =
                inventoryBalanceRepository
                        .findBySkuIdAndWarehouseIdAndBinId(
                                event.getSkuId(),
                                event.getWarehouseId(),
                                event.getTargetBinId()
                        )
                        .orElseGet(() -> createNewBalance(event, now));

        // Update the inventory balance.
        balance.setAvailableQuantity(
                balance.getAvailableQuantity().add(event.getQuantity())
        );
        balance.setUpdatedAt(now);

        inventoryBalanceRepository.save(balance);

        // Record the corresponding stock movement.
        InventoryMovement movement = new InventoryMovement();

        movement.setId(UUID.randomUUID());
        movement.setSkuId(event.getSkuId());
        movement.setWarehouseId(event.getWarehouseId());
        movement.setBinId(event.getTargetBinId());
        movement.setMovementType("PUTAWAY");
        movement.setQuantity(event.getQuantity());
        movement.setReferenceType("PUTAWAY_TASK");
        movement.setReferenceId(event.getPutAwayTaskId().toString());
        movement.setEventId(event.getEventId());
        movement.setCreatedAt(now);

        inventoryMovementRepository.save(movement);

        // Save the InventoryUpdated event in the same transaction.
        outboxEventService.createInventoryUpdatedEvent(event, balance);
    }

    private void validateEvent(PutAwayCompletedEvent event) {

        if (event == null) {
            throw new IllegalArgumentException(
                    "PutAwayCompleted event cannot be null");
        }

        if (event.getEventId() == null) {
            throw new IllegalArgumentException(
                    "Event ID is required");
        }

        if (!"PutAwayCompleted".equals(event.getEventType())) {
            throw new IllegalArgumentException(
                    "Invalid event type");
        }

        if (event.getEventVersion() == null
                || event.getEventVersion() < 1) {
            throw new IllegalArgumentException(
                    "A valid event version is required");
        }

        if (event.getOccurredAt() == null
                || event.getSource() == null
                || event.getSource().isBlank()) {
            throw new IllegalArgumentException(
                    "Event timestamp and source are required");
        }

        if (event.getPutAwayTaskId() == null
                || event.getSkuId() == null
                || event.getWarehouseId() == null
                || event.getTargetBinId() == null) {
            throw new IllegalArgumentException(
                    "Task, SKU, warehouse and target bin IDs are required");
        }

        if (event.getQuantity() == null
                || event.getQuantity().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException(
                    "Put-away quantity must be greater than zero");
        }
    }

    private InventoryBalance createNewBalance(
            PutAwayCompletedEvent event,
            LocalDateTime now) {

        InventoryBalance balance = new InventoryBalance();

        balance.setId(UUID.randomUUID());
        balance.setSkuId(event.getSkuId());
        balance.setWarehouseId(event.getWarehouseId());
        balance.setBinId(event.getTargetBinId());
        balance.setAvailableQuantity(BigDecimal.ZERO);
        balance.setReservedQuantity(BigDecimal.ZERO);
        balance.setUpdatedAt(now);

        return balance;
    }
}
