package com.company.wms.inbound_service.mapper;

import java.util.List;

import org.springframework.stereotype.Component;

import com.company.wms.inbound_service.domain.GoodsReceipt;
import com.company.wms.inbound_service.domain.GoodsReceiptLine;
import com.company.wms.inbound_service.dto.CreateGoodsReceiptLineRequest;
import com.company.wms.inbound_service.dto.CreateGoodsReceiptRequest;
import com.company.wms.inbound_service.dto.GoodsReceiptLineResponse;
import com.company.wms.inbound_service.dto.GoodsReceiptResponse;

@Component
public class GoodsReceiptMapper {

    public GoodsReceipt toEntity(CreateGoodsReceiptRequest request) {

        GoodsReceipt goodsReceipt = new GoodsReceipt();

        goodsReceipt.setWarehouseId(request.getWarehouseId());
        goodsReceipt.setReferenceType(request.getReferenceType());
        goodsReceipt.setReferenceId(request.getReferenceId());

        return goodsReceipt;
    }

    public GoodsReceiptLine toLineEntity(
            CreateGoodsReceiptLineRequest request) {

        GoodsReceiptLine line = new GoodsReceiptLine();

        line.setSkuId(request.getSkuId());
        line.setExpectedQuantity(request.getExpectedQuantity());
        line.setReceivedQuantity(request.getReceivedQuantity());
        line.setUom(request.getUom());
        line.setBatchNumber(request.getBatchNumber());
        line.setSerialNumber(request.getSerialNumber());

        return line;
    }

    public GoodsReceiptResponse toResponse(
            GoodsReceipt goodsReceipt,
            List<GoodsReceiptLine> lines) {

        GoodsReceiptResponse response = new GoodsReceiptResponse();

        response.setId(goodsReceipt.getId());
        response.setReceiptNumber(goodsReceipt.getReceiptNumber());
        response.setWarehouseId(goodsReceipt.getWarehouseId());
        response.setReferenceType(goodsReceipt.getReferenceType());
        response.setReferenceId(goodsReceipt.getReferenceId());
        response.setStatus(goodsReceipt.getStatus());
        response.setReceivedAt(goodsReceipt.getReceivedAt());
        response.setCreatedAt(goodsReceipt.getCreatedAt());
        response.setUpdatedAt(goodsReceipt.getUpdatedAt());

        response.setLines(
                lines.stream()
                        .map(this::toLineResponse)
                        .toList()
        );

        return response;
    }

    private GoodsReceiptLineResponse toLineResponse(
            GoodsReceiptLine line) {

        GoodsReceiptLineResponse response =
                new GoodsReceiptLineResponse();

        response.setId(line.getId());
        response.setSkuId(line.getSkuId());
        response.setExpectedQuantity(line.getExpectedQuantity());
        response.setReceivedQuantity(line.getReceivedQuantity());
        response.setUom(line.getUom());
        response.setBatchNumber(line.getBatchNumber());
        response.setSerialNumber(line.getSerialNumber());
        response.setStatus(line.getStatus());

        return response;
    }
}