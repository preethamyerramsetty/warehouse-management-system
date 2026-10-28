
package com.company.wms.putaway_service.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public class PutAwayCompletedEvent {

    private UUID eventId;
    private String eventType;
    private Integer eventVersion;
    private LocalDateTime occurredAt;
    private String source;
    private String correlationId;
    private String entityId;
    private Payload payload;

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

    public String getEntityId() {
        return entityId;
    }

    public void setEntityId(String entityId) {
        this.entityId = entityId;
    }

    public Payload getPayload() {
        return payload;
    }

    public void setPayload(Payload payload) {
        this.payload = payload;
    }

    public static class Payload {

        private UUID putAwayTaskId;
        private String taskNumber;
        private UUID goodsReceiptLineId;
        private UUID skuId;
        private BigDecimal quantity;
        private UUID warehouseId;
        private UUID targetBinId;

        public UUID getPutAwayTaskId() {
            return putAwayTaskId;
        }

        public void setPutAwayTaskId(UUID putAwayTaskId) {
            this.putAwayTaskId = putAwayTaskId;
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

        public UUID getWarehouseId() {
            return warehouseId;
        }

        public void setWarehouseId(UUID warehouseId) {
            this.warehouseId = warehouseId;
        }

        public UUID getTargetBinId() {
            return targetBinId;
        }

        public void setTargetBinId(UUID targetBinId) {
            this.targetBinId = targetBinId;
        }
    }
}
