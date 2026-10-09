package com.company.wms.inventory_service.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.company.wms.inventory_service.dto.InventoryBalanceResponse;
import com.company.wms.inventory_service.service.InventoryQueryService;

@RestController
@RequestMapping("/api/v1/inventory")
public class InventoryQueryController {

    private final InventoryQueryService inventoryQueryService;

    public InventoryQueryController(
            InventoryQueryService inventoryQueryService) {

        this.inventoryQueryService = inventoryQueryService;
    }

    @GetMapping("/sku/{skuId}/warehouse/{warehouseId}/bin/{binId}")
    public InventoryBalanceResponse getInventoryBalance(
            @PathVariable UUID skuId,
            @PathVariable UUID warehouseId,
            @PathVariable UUID binId) {

        return inventoryQueryService.getInventoryBalance(
                skuId,
                warehouseId,
                binId
        );
    }

    @GetMapping("/sku/{skuId}/warehouse/{warehouseId}")
    public List<InventoryBalanceResponse> getInventoryBySkuAndWarehouse(
            @PathVariable UUID skuId,
            @PathVariable UUID warehouseId) {

        return inventoryQueryService.getInventoryBySkuAndWarehouse(
                skuId, warehouseId);
    }

    @GetMapping("/warehouse/{warehouseId}")
    public List<InventoryBalanceResponse> getInventoryByWarehouse(
            @PathVariable UUID warehouseId) {

        return inventoryQueryService.getInventoryByWarehouse(warehouseId);
    }
}