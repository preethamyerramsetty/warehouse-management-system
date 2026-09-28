package com.company.wms.inbound_service.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.company.wms.inbound_service.domain.GoodsReceipt;

public interface GoodsReceiptRepository
        extends JpaRepository<GoodsReceipt, UUID> {

    boolean existsByReceiptNumber(String receiptNumber);
}