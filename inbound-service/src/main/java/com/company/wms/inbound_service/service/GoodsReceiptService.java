package com.company.wms.inbound_service.service;

import com.company.wms.inbound_service.domain.GoodsReceipt;
import com.company.wms.inbound_service.domain.GoodsReceiptLine;
import com.company.wms.inbound_service.domain.GoodsReceiptLineStatus;
import com.company.wms.inbound_service.domain.GoodsReceiptStatus;
import com.company.wms.inbound_service.dto.CreateGoodsReceiptRequest;
import com.company.wms.inbound_service.dto.GoodsReceiptResponse;
import com.company.wms.inbound_service.exception.DuplicateResourceException;
import com.company.wms.inbound_service.exception.ResourceNotFoundException;
import com.company.wms.inbound_service.mapper.GoodsReceiptMapper;
import com.company.wms.inbound_service.repository.GoodsReceiptLineRepository;
import com.company.wms.inbound_service.repository.GoodsReceiptRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class GoodsReceiptService {

    private final GoodsReceiptRepository goodsReceiptRepository;
    private final GoodsReceiptLineRepository goodsReceiptLineRepository;
    private final GoodsReceiptMapper goodsReceiptMapper;

    public GoodsReceiptService(
            GoodsReceiptRepository goodsReceiptRepository,
            GoodsReceiptLineRepository goodsReceiptLineRepository,
            GoodsReceiptMapper goodsReceiptMapper) {

        this.goodsReceiptRepository = goodsReceiptRepository;
        this.goodsReceiptLineRepository = goodsReceiptLineRepository;
        this.goodsReceiptMapper = goodsReceiptMapper;
    }

    @Transactional
    public GoodsReceiptResponse createGoodsReceipt(
            CreateGoodsReceiptRequest request) {

        // 1. Generate receipt number
        String receiptNumber = generateReceiptNumber();

        // 2. Create receipt header
        GoodsReceipt goodsReceipt =
                goodsReceiptMapper.toEntity(request);

        goodsReceipt.setReceiptNumber(receiptNumber);
        goodsReceipt.setStatus(GoodsReceiptStatus.RECEIVED);

        LocalDateTime now = LocalDateTime.now();

        goodsReceipt.setReceivedAt(now);
        goodsReceipt.setCreatedAt(now);
        goodsReceipt.setUpdatedAt(now);

        // 3. Save receipt header
        GoodsReceipt savedReceipt =
                goodsReceiptRepository.save(goodsReceipt);

        // 4. Create and save receipt lines
        List<GoodsReceiptLine> lines =
                request.getLines()
                        .stream()
                        .map(lineRequest -> {

                            GoodsReceiptLine line =
                                    goodsReceiptMapper
                                            .toLineEntity(lineRequest);

                            line.setGoodsReceiptId(
                                    savedReceipt.getId()
                            );

                            line.setStatus(
                                    determineLineStatus(
                                            lineRequest.getExpectedQuantity(),
                                            lineRequest.getReceivedQuantity()
                                    )
                            );

                            return line;
                        })
                        .toList();

        List<GoodsReceiptLine> savedLines =
                goodsReceiptLineRepository.saveAll(lines);

        // 5. Return complete receipt
        return goodsReceiptMapper.toResponse(
                savedReceipt,
                savedLines
        );
    }

    @Transactional(readOnly = true)
    public GoodsReceiptResponse getGoodsReceipt(UUID id) {

        GoodsReceipt goodsReceipt =
                goodsReceiptRepository.findById(id)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Goods Receipt not found: " + id
                                ));

        List<GoodsReceiptLine> lines =
                goodsReceiptLineRepository
                        .findByGoodsReceiptId(id);

        return goodsReceiptMapper.toResponse(
                goodsReceipt,
                lines
        );
    }

    private GoodsReceiptLineStatus determineLineStatus(
            java.math.BigDecimal expectedQuantity,
            java.math.BigDecimal receivedQuantity) {

        int comparison =
                receivedQuantity.compareTo(expectedQuantity);

        if (comparison >= 0) {
            return GoodsReceiptLineStatus.RECEIVED;
        }

        return GoodsReceiptLineStatus.PARTIALLY_RECEIVED;
    }

    private String generateReceiptNumber() {

        return "GR-" +
                UUID.randomUUID()
                        .toString()
                        .substring(0, 8)
                        .toUpperCase();
    }
}