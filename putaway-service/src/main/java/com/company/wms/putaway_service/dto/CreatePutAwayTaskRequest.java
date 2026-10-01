package com.company.wms.putaway_service.dto;

import java.math.BigDecimal;
import java.util.UUID;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

public class CreatePutAwayTaskRequest {

    @NotNull
    private UUID goodsReceiptLineId;

    @NotNull
    private UUID skuId;

    @NotNull
    @DecimalMin(value = "0.0", inclusive = false)
    private BigDecimal quantity;

    @NotNull
    private UUID targetBinId;

    private String assignedTo;

    public CreatePutAwayTaskRequest() {
    }

    public UUID getGoodsReceiptLineId() {
        return goodsReceiptLineId;
    }

    public void setGoodsReceiptLineId(UUID goodsReceiptLineId) {
        this.goodsReceiptLineId = goodsReceiptLineId;
    }

    public UUID getSkuId() {
        return skuId;
    }

    public void setSkuId(UUID skuId) {
        this.skuId = skuId;
    }

    public BigDecimal getQuantity() {
        return quantity;
    }

    public void setQuantity(BigDecimal quantity) {
        this.quantity = quantity;
    }

    public UUID getTargetBinId() {
        return targetBinId;
    }

    public void setTargetBinId(UUID targetBinId) {
        this.targetBinId = targetBinId;
    }

    public String getAssignedTo() {
        return assignedTo;
    }

    public void setAssignedTo(String assignedTo) {
        this.assignedTo = assignedTo;
    }
}