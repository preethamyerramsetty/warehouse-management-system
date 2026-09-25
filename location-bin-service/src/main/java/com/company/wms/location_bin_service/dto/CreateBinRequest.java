package com.company.wms.location_bin_service.dto;

import java.math.BigDecimal;
import java.util.UUID;

import com.company.wms.location_bin_service.domain.BinStatus;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class CreateBinRequest {

    @NotNull(message = "Location ID is required")
    private UUID locationId;

    @NotBlank(message = "Bin code is required")
    @Size(max = 50, message = "Bin code must not exceed 50 characters")
    private String binCode;

    @NotNull(message = "Capacity is required")
    @DecimalMin(value = "0.0", inclusive = false,
            message = "Capacity must be greater than zero")
    private BigDecimal capacity;

    @NotNull(message = "Bin status is required")
    private BinStatus status;

    public CreateBinRequest() {
    }

    public UUID getLocationId() {
        return locationId;
    }

    public void setLocationId(UUID locationId) {
        this.locationId = locationId;
    }

    public String getBinCode() {
        return binCode;
    }

    public void setBinCode(String binCode) {
        this.binCode = binCode;
    }

    public BigDecimal getCapacity() {
        return capacity;
    }

    public void setCapacity(BigDecimal capacity) {
        this.capacity = capacity;
    }

    public BinStatus getStatus() {
        return status;
    }

    public void setStatus(BinStatus status) {
        this.status = status;
    }
}