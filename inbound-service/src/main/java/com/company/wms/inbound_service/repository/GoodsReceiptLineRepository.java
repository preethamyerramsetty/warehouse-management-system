package com.company.wms.inbound_service.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.company.wms.inbound_service.domain.GoodsReceiptLine;

public interface GoodsReceiptLineRepository
        extends JpaRepository<GoodsReceiptLine, UUID> {

    List<GoodsReceiptLine> findByGoodsReceiptId(UUID goodsReceiptId);
}