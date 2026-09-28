package com.company.wms.inbound_service.controller;

import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.company.wms.inbound_service.dto.CreateGoodsReceiptRequest;
import com.company.wms.inbound_service.dto.GoodsReceiptResponse;
import com.company.wms.inbound_service.service.GoodsReceiptService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/goods-receipts")
public class GoodsReceiptController {

    private final GoodsReceiptService goodsReceiptService;

    public GoodsReceiptController(
            GoodsReceiptService goodsReceiptService) {
        this.goodsReceiptService = goodsReceiptService;
    }

    @PostMapping
    public ResponseEntity<GoodsReceiptResponse> createGoodsReceipt(
            @Valid @RequestBody CreateGoodsReceiptRequest request) {

        GoodsReceiptResponse response =
                goodsReceiptService.createGoodsReceipt(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<GoodsReceiptResponse> getGoodsReceipt(
            @PathVariable UUID id) {

        GoodsReceiptResponse response =
                goodsReceiptService.getGoodsReceipt(id);

        return ResponseEntity.ok(response);
    }
}