package com.company.wms.inbound_service.dto;

import java.util.List;
import java.util.UUID;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class CreateGoodsReceiptRequest {

    @NotNull(message = "Warehouse ID is required")
    private UUID warehouseId;

    @NotBlank(message = "Reference type is required")
    @Size(max = 50, message = "Reference type must not exceed 50 characters")
    private String referenceType;

    @NotBlank(message = "Reference ID is required")
    @Size(max = 100, message = "Reference ID must not exceed 100 characters")
    private String referenceId;

    @NotEmpty(message = "At least one receipt line is required")
    @Valid
    private List<CreateGoodsReceiptLineRequest> lines;

    public CreateGoodsReceiptRequest() {
    }

    public UUID getWarehouseId() {
        return warehouseId;
    }

    public void setWarehouseId(UUID warehouseId) {
        this.warehouseId = warehouseId;
    }

    public String getReferenceType() {
        return referenceType;
    }

    public void setReferenceType(String referenceType) {
        this.referenceType = referenceType;
    }

    public String getReferenceId() {
        return referenceId;
    }

    public void setReferenceId(String referenceId) {
        this.referenceId = referenceId;
    }

    public List<CreateGoodsReceiptLineRequest> getLines() {
        return lines;
    }

    public void setLines(List<CreateGoodsReceiptLineRequest> lines) {
        this.lines = lines;
    }
}