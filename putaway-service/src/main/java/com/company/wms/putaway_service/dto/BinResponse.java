package com.company.wms.putaway_service.dto;

import java.math.BigDecimal;
import java.util.UUID;

public record BinResponse(
        UUID id,
        UUID locationId,
        String binCode,
        BigDecimal capacity,
        String status
) {
}