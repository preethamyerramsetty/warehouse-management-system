package com.company.wms.location_bin_service.dto;

import com.company.wms.location_bin_service.domain.LocationStatus;
import com.company.wms.location_bin_service.domain.LocationType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.UUID;

public class CreateLocationRequest {

    @NotNull(message = "Warehouse ID is required")
    private UUID warehouseId;

    @NotBlank(message = "Location code is required")
    @Size(max = 50, message = "Location code must not exceed 50 characters")
    private String locationCode;

    @NotBlank(message = "Location name is required")
    @Size(max = 100, message = "Location name must not exceed 100 characters")
    private String name;

    @NotNull(message = "Location type is required")
    private LocationType type;

    @NotNull(message = "Location status is required")
    private LocationStatus status;

    public CreateLocationRequest() {
    }

    public UUID getWarehouseId() {
        return warehouseId;
    }

    public void setWarehouseId(UUID warehouseId) {
        this.warehouseId = warehouseId;
    }

    public String getLocationCode() {
        return locationCode;
    }

    public void setLocationCode(String locationCode) {
        this.locationCode = locationCode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocationType getType() {
        return type;
    }

    public void setType(LocationType type) {
        this.type = type;
    }

    public LocationStatus getStatus() {
        return status;
    }

    public void setStatus(LocationStatus status) {
        this.status = status;
    }
}