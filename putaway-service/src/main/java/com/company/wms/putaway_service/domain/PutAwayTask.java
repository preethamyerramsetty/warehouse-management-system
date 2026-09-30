package com.company.wms.putaway_service.domain;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(
    name = "putaway_task",
    uniqueConstraints = {
        @UniqueConstraint(
            name = "uk_putaway_task_number",
            columnNames = "task_number"
        )
    }
)
public class PutAwayTask {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "task_number", nullable = false, length = 50)
    private String taskNumber;

    @Column(name = "goods_receipt_line_id", nullable = false)
    private UUID goodsReceiptLineId;

    @Column(name = "sku_id", nullable = false)
    private UUID skuId;

    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal quantity;

    @Column(name = "target_bin_id", nullable = false)
    private UUID targetBinId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private PutAwayTaskStatus status;

    @Column(name = "assigned_to", length = 100)
    private String assignedTo;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "completed_at")
    private LocalDateTime completedAt;

    public PutAwayTask() {
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