package com.company.wms.putaway_service.domain;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.persistence.Version;

@Entity
@Table(
        name = "putaway_receipt_line",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_putaway_receipt_line",
                        columnNames = "goods_receipt_line_id"
                )
        }
)
public class PutAwayReceiptLine {

    @Id
    private UUID id;

    @Column(name = "goods_receipt_line_id", nullable = false)
    private UUID goodsReceiptLineId;

    @Column(name = "sku_id", nullable = false)
    private UUID skuId;

    @Column(name = "received_quantity", nullable = false)
    private BigDecimal receivedQuantity;

    @Column(name = "putaway_quantity", nullable = false)
    private BigDecimal putawayQuantity;

    @Column(name = "remaining_quantity", nullable = false)
    private BigDecimal remainingQuantity;

    @Version
    @Column(nullable = false)
    private Long version;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
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

    public BigDecimal getReceivedQuantity() {
        return receivedQuantity;
    }

    public void setReceivedQuantity(BigDecimal receivedQuantity) {
        this.receivedQuantity = receivedQuantity;
    }

    public BigDecimal getPutawayQuantity() {
        return putawayQuantity;
    }

    public void setPutawayQuantity(BigDecimal putawayQuantity) {
        this.putawayQuantity = putawayQuantity;
    }

    public BigDecimal getRemainingQuantity() {
        return remainingQuantity;
    }

    public void setRemainingQuantity(BigDecimal remainingQuantity) {
        this.remainingQuantity = remainingQuantity;
    }

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}