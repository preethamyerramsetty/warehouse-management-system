package com.company.wms.putaway_service.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.company.wms.putaway_service.domain.PutAwayReceiptLine;

public interface PutAwayReceiptLineRepository
        extends JpaRepository<PutAwayReceiptLine, UUID> {

    Optional<PutAwayReceiptLine>
    findByGoodsReceiptLineId(UUID goodsReceiptLineId);
}