package com.company.wms.inbound_service.dto;

import java.math.BigDecimal;
import java.util.UUID;

import com.company.wms.inbound_service.domain.GoodsReceiptLineStatus;

public class GoodsReceiptLineResponse {

    private UUID id;
    private UUID skuId;
    private BigDecimal expectedQuantity;
    private BigDecimal receivedQuantity;
    private String uom;
    private String batchNumber;
    private String serialNumber;
    private GoodsReceiptLineStatus status;

    public GoodsReceiptLineResponse() {
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
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

    public GoodsReceiptLineStatus getStatus() {
        return status;
    }

    public void setStatus(GoodsReceiptLineStatus status) {
        this.status = status;
    }
}