package com.company.wms.putaway_service.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import com.company.wms.putaway_service.domain.PutAwayTaskStatus;

public class PutAwayTaskResponse {

    private UUID id;
    private String taskNumber;
    private UUID goodsReceiptLineId;
    private UUID skuId;
    private BigDecimal quantity;
    private UUID targetBinId;
    private PutAwayTaskStatus status;
    private String assignedTo;
    private LocalDateTime createdAt;
    private LocalDateTime completedAt;

    public PutAwayTaskResponse() {
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getTaskNumber() {
        return taskNumber;
    }

    public void setTaskNumber(String taskNumber) {
        this.taskNumber = taskNumber;
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

    public PutAwayTaskStatus getStatus() {
        return status;
    }

    public void setStatus(PutAwayTaskStatus status) {
        this.status = status;
    }

    public String getAssignedTo() {
        return assignedTo;
    }

    public void setAssignedTo(String assignedTo) {
        this.assignedTo = assignedTo;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getCompletedAt() {
        return completedAt;
    }

    public void setCompletedAt(LocalDateTime completedAt) {
        this.completedAt = completedAt;
    }
}