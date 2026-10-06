package com.company.wms.inventory_service.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.company.wms.inventory_service.domain.InventoryMovement;

public interface InventoryMovementRepository
        extends JpaRepository<InventoryMovement, UUID> {

    boolean existsByEventId(UUID eventId);
}