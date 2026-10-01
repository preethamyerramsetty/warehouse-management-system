package com.company.wms.putaway_service.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public class GoodsReceivedEvent {

    private UUID eventId;
    private String eventType;
    private Integer eventVersion;
    private LocalDateTime occurredAt;
    private String source;
    private String correlationId;
    private UUID goodsReceiptId;
    private UUID warehouseId;
    private String receiptNumber;
    private List<GoodsReceivedLineEvent> lines;

    public UUID getEventId() {
        return eventId;
    }

    public void setEventId(UUID eventId) {
        this.eventId = eventId;
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public Integer getEventVersion() {
        return eventVersion;
    }

    public void setEventVersion(Integer eventVersion) {
        this.eventVersion = eventVersion;
    }

    public LocalDateTime getOccurredAt() {
        return occurredAt;
    }

    public void setOccurredAt(LocalDateTime occurredAt) {
        this.occurredAt = occurredAt;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getCorrelationId() {
        return correlationId;
    }

    public void setCorrelationId(String correlationId) {
        this.correlationId = correlationId;
    }

    public UUID getGoodsReceiptId() {
        return goodsReceiptId;
    }

    public void setGoodsReceiptId(UUID goodsReceiptId) {
        this.goodsReceiptId = goodsReceiptId;
    }

    public UUID getWarehouseId() {
        return warehouseId;
    }

    public void setWarehouseId(UUID warehouseId) {
        this.warehouseId = warehouseId;
    }

    public String getReceiptNumber() {
        return receiptNumber;
    }

    public void setReceiptNumber(String receiptNumber) {
        this.receiptNumber = receiptNumber;
    }

    public List<GoodsReceivedLineEvent> getLines() {
        return lines;
    }

    public void setLines(List<GoodsReceivedLineEvent> lines) {
        this.lines = lines;
    }

    public static class GoodsReceivedLineEvent {

        private UUID goodsReceiptLineId;
        private UUID skuId;
        private BigDecimal quantity;
        private String uom;
        private String batchNumber;
        private String serialNumber;

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
}