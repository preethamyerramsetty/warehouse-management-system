package com.company.wms.inbound_service.client;

import java.util.UUID;

public record WarehouseResponse(
        UUID id,
        String warehouseCode,
        String name,
        String address,
        String status
) {
}