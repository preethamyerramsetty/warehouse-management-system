package com.company.wms.inventory_service.service;

import com.company.wms.inventory_service.domain.InventoryBalance;
import com.company.wms.inventory_service.domain.InventoryMovement;
import com.company.wms.inventory_service.dto.PutAwayCompletedEvent;
import com.company.wms.inventory_service.repository.InventoryBalanceRepository;
import com.company.wms.inventory_service.repository.InventoryMovementRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

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

        // Idempotency check
        if (inventoryMovementRepository.existsByEventId(event.getEventId())) {
            return;
        }

        InventoryBalance balance =
                inventoryBalanceRepository
                        .findBySkuIdAndWarehouseIdAndBinId(
                                event.getSkuId(),
                                event.getWarehouseId(),
                                event.getTargetBinId()
                        )
                        .orElseGet(() -> createNewBalance(event));

        BigDecimal currentQuantity = balance.getAvailableQuantity();

        balance.setAvailableQuantity(
                currentQuantity.add(event.getQuantity())
        );

        balance.setUpdatedAt(LocalDateTime.now());

        inventoryBalanceRepository.save(balance);

        // Record the stock movement
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
        movement.setCreatedAt(LocalDateTime.now());

        inventoryMovementRepository.save(movement);

        outboxEventService.createInventoryUpdatedEvent(
                event,
                balance
        );
    }

    private InventoryBalance createNewBalance(
            PutAwayCompletedEvent event) {

        InventoryBalance balance = new InventoryBalance();

        balance.setId(UUID.randomUUID());
        balance.setSkuId(event.getSkuId());
        balance.setWarehouseId(event.getWarehouseId());
        balance.setBinId(event.getTargetBinId());
        balance.setAvailableQuantity(BigDecimal.ZERO);
        balance.setReservedQuantity(BigDecimal.ZERO);
        balance.setUpdatedAt(LocalDateTime.now());

        return balance;
    }
}