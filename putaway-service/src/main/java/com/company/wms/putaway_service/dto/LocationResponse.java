package com.company.wms.putaway_service.dto;

import java.util.UUID;

public record LocationResponse(
        UUID id,
        UUID warehouseId,
        String locationCode,
        String name,
        String type,
        String status
) {
}