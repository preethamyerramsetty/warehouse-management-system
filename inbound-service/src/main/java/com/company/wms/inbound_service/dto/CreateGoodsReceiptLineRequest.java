package com.company.wms.inbound_service.dto;

import java.math.BigDecimal;
import java.util.UUID;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class CreateGoodsReceiptLineRequest {

    @NotNull(message = "SKU ID is required")
    private UUID skuId;

    @NotNull(message = "Expected quantity is required")
    @DecimalMin(value = "0.0", inclusive = false,
            message = "Expected quantity must be greater than zero")
    private BigDecimal expectedQuantity;

    @NotNull(message = "Received quantity is required")
    @DecimalMin(value = "0.0", inclusive = false,
            message = "Received quantity must be greater than zero")
    private BigDecimal receivedQuantity;

    @NotBlank(message = "UOM is required")
    @Size(max = 20, message = "UOM must not exceed 20 characters")
    private String uom;

    @Size(max = 100, message = "Batch number must not exceed 100 characters")
    private String batchNumber;

    @Size(max = 100, message = "Serial number must not exceed 100 characters")
    private String serialNumber;

    public CreateGoodsReceiptLineRequest() {
    }

    public UUID getSkuId() {
        return skuId;
    }

    public void setSkuId(UUID skuId) {
        this.skuId = skuId;
    }

    public BigDecimal getExpectedQuantity() {
        return expectedQuantity;
    }

    public void setExpectedQuantity(BigDecimal expectedQuantity) {
        this.expectedQuantity = expectedQuantity;
    }

    public BigDecimal getReceivedQuantity() {
        return receivedQuantity;
    }

    public void setReceivedQuantity(BigDecimal receivedQuantity) {
        this.receivedQuantity = receivedQuantity;
    }

    public String getUom() {
        return uom;
    }

    public void setUom(String uom) {
        this.uom = uom;
    }

    public String getBatchNumber() {
        return batchNumber;
    }

    public void setBatchNumber(String batchNumber) {
        this.batchNumber = batchNumber;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }
}