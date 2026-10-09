package com.company.wms.inventory_service.service;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.company.wms.inventory_service.domain.InventoryBalance;
import com.company.wms.inventory_service.dto.InventoryBalanceResponse;
import com.company.wms.inventory_service.repository.InventoryBalanceRepository;

@Service
public class InventoryQueryService {

    private final InventoryBalanceRepository inventoryBalanceRepository;

    public InventoryQueryService(
            InventoryBalanceRepository inventoryBalanceRepository) {

        this.inventoryBalanceRepository = inventoryBalanceRepository;
    }

    @Transactional(readOnly = true)
    public InventoryBalanceResponse getInventoryBalance(
            UUID skuId,
            UUID warehouseId,
            UUID binId) {

        InventoryBalance balance =
                inventoryBalanceRepository
                        .findBySkuIdAndWarehouseIdAndBinId(
                                skuId,
                                warehouseId,
                                binId
                        )
                        .orElseThrow(() ->
                                new IllegalArgumentException(
                                        "Inventory balance not found"
                                )
                        );

        InventoryBalanceResponse response =
                new InventoryBalanceResponse();

        response.setSkuId(balance.getSkuId());
        response.setWarehouseId(balance.getWarehouseId());
        response.setBinId(balance.getBinId());
        response.setAvailableQuantity(
                balance.getAvailableQuantity()
        );
        response.setReservedQuantity(
                balance.getReservedQuantity()
        );
        response.setUpdatedAt(balance.getUpdatedAt());

        return response;
    }

    @Transactional(readOnly = true)
        public List<InventoryBalanceResponse> getInventoryBySkuAndWarehouse(
                UUID skuId,
                UUID warehouseId) {

        List<InventoryBalance> balances =
                inventoryBalanceRepository.findBySkuIdAndWarehouseId(
                        skuId, warehouseId);

        return balances.stream()
                .map(balance -> {
                        InventoryBalanceResponse response =
                                new InventoryBalanceResponse();

                        response.setSkuId(balance.getSkuId());
                        response.setWarehouseId(balance.getWarehouseId());
                        response.setBinId(balance.getBinId());
                        response.setAvailableQuantity(
                                balance.getAvailableQuantity());
                        response.setReservedQuantity(
                                balance.getReservedQuantity());
                        response.setUpdatedAt(balance.getUpdatedAt());

                        return response;
                })
                .toList();
        }

        @Transactional(readOnly = true)
        public List<InventoryBalanceResponse> getInventoryByWarehouse(
                UUID warehouseId) {

        List<InventoryBalance> balances =
                inventoryBalanceRepository.findByWarehouseId(warehouseId);

        return balances.stream()
                .map(balance -> {
                        InventoryBalanceResponse response =
                                new InventoryBalanceResponse();

                        response.setSkuId(balance.getSkuId());
                        response.setWarehouseId(balance.getWarehouseId());
                        response.setBinId(balance.getBinId());
                        response.setAvailableQuantity(
                                balance.getAvailableQuantity());
                        response.setReservedQuantity(
                                balance.getReservedQuantity());
                        response.setUpdatedAt(balance.getUpdatedAt());

                        return response;
                })
                .toList();
        }
}