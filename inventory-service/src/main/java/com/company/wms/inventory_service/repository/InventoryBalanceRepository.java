package com.company.wms.inventory_service.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.company.wms.inventory_service.domain.InventoryBalance;

public interface InventoryBalanceRepository
        extends JpaRepository<InventoryBalance, UUID> {

    Optional<InventoryBalance> findBySkuIdAndWarehouseIdAndBinId(
            UUID skuId,
            UUID warehouseId,
            UUID binId
    );

    List<InventoryBalance> findBySkuIdAndWarehouseId(
                UUID skuId,
                UUID warehouseId
        );

    List<InventoryBalance> findByWarehouseId(UUID warehouseId);
}