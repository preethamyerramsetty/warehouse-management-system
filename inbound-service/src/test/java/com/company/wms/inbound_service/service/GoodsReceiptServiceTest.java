
package com.company.wms.inbound_service.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.eq;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;

import com.company.wms.inbound_service.client.WarehouseClient;
import com.company.wms.inbound_service.client.WarehouseResponse;
import com.company.wms.inbound_service.domain.GoodsReceipt;
import com.company.wms.inbound_service.domain.GoodsReceiptLine;
import com.company.wms.inbound_service.domain.GoodsReceiptLineStatus;
import com.company.wms.inbound_service.domain.GoodsReceiptStatus;
import com.company.wms.inbound_service.dto.CreateGoodsReceiptLineRequest;
import com.company.wms.inbound_service.dto.CreateGoodsReceiptRequest;
import com.company.wms.inbound_service.dto.GoodsReceiptResponse;
import com.company.wms.inbound_service.mapper.GoodsReceiptMapper;
import com.company.wms.inbound_service.repository.GoodsReceiptLineRepository;
import com.company.wms.inbound_service.repository.GoodsReceiptRepository;

@ExtendWith(MockitoExtension.class)
class GoodsReceiptServiceTest {

    @Mock
    private GoodsReceiptRepository goodsReceiptRepository;

    @Mock
    private GoodsReceiptLineRepository goodsReceiptLineRepository;

    @Mock
    private GoodsReceiptMapper goodsReceiptMapper;

    @Mock
    private OutboxEventService outboxEventService;

    @Mock
    private WarehouseClient warehouseClient;

    @Mock
    private WarehouseResponse warehouseResponse;

    @InjectMocks
    private GoodsReceiptService goodsReceiptService;

    @Test
    void createGoodsReceipt_withValidRequest_savesReceiptAndPublishesEvent() {
        UUID receiptId = UUID.randomUUID();
        UUID warehouseId = UUID.randomUUID();
        UUID skuId = UUID.randomUUID();
        String correlationId = "test-correlation-id";

        CreateGoodsReceiptLineRequest lineRequest =
                new CreateGoodsReceiptLineRequest();
        lineRequest.setSkuId(skuId);
        lineRequest.setExpectedQuantity(new BigDecimal("10"));
        lineRequest.setReceivedQuantity(new BigDecimal("10"));
        lineRequest.setUom("EA");

        CreateGoodsReceiptRequest request = new CreateGoodsReceiptRequest();
        request.setWarehouseId(warehouseId);
        request.setReferenceType("PURCHASE_ORDER");
        request.setReferenceId("PO-1001");
        request.setLines(List.of(lineRequest));

        GoodsReceipt receipt = new GoodsReceipt();
        receipt.setId(receiptId);
        receipt.setWarehouseId(warehouseId);

        GoodsReceiptLine line = new GoodsReceiptLine();
        line.setSkuId(skuId);
        line.setExpectedQuantity(new BigDecimal("10"));
        line.setReceivedQuantity(new BigDecimal("10"));
        line.setUom("EA");

        GoodsReceiptResponse expectedResponse = new GoodsReceiptResponse();

        when(warehouseClient.getWarehouse(warehouseId))
                .thenReturn(warehouseResponse);
        when(goodsReceiptMapper.toEntity(request)).thenReturn(receipt);
        when(goodsReceiptRepository.save(any(GoodsReceipt.class)))
                .thenAnswer(invocation -> {
                    GoodsReceipt saved = invocation.getArgument(0);
                    saved.setId(receiptId);
                    return saved;
                });
        when(goodsReceiptMapper.toLineEntity(lineRequest)).thenReturn(line);
        when(goodsReceiptLineRepository.saveAll(anyList()))
                .thenAnswer(invocation -> invocation.getArgument(0));
        when(goodsReceiptMapper.toResponse(eq(receipt), anyList()))
                .thenReturn(expectedResponse);

        GoodsReceiptResponse actualResponse =
                goodsReceiptService.createGoodsReceipt(request, correlationId);

        assertSame(expectedResponse, actualResponse);
        assertEquals(GoodsReceiptStatus.RECEIVED, receipt.getStatus());
        assertEquals(GoodsReceiptLineStatus.RECEIVED, line.getStatus());
        assertEquals(receiptId, line.getGoodsReceiptId());

        verify(warehouseClient).getWarehouse(warehouseId);
        verify(goodsReceiptRepository).save(receipt);
        verify(goodsReceiptLineRepository).saveAll(anyList());
        verify(outboxEventService)
                .createGoodsReceivedEvent(
                        eq(receipt), anyList(), eq(correlationId));
    }

    @Test
    void createGoodsReceipt_whenSavingLinesFails_doesNotPublishEvent() {
        UUID receiptId = UUID.randomUUID();
        UUID warehouseId = UUID.randomUUID();
        UUID skuId = UUID.randomUUID();
        String correlationId = "test-correlation-id";

        CreateGoodsReceiptLineRequest lineRequest =
                new CreateGoodsReceiptLineRequest();
        lineRequest.setSkuId(skuId);
        lineRequest.setExpectedQuantity(new BigDecimal("10"));
        lineRequest.setReceivedQuantity(new BigDecimal("10"));
        lineRequest.setUom("EA");

        CreateGoodsReceiptRequest request = new CreateGoodsReceiptRequest();
        request.setWarehouseId(warehouseId);
        request.setReferenceType("PURCHASE_ORDER");
        request.setReferenceId("PO-1001");
        request.setLines(List.of(lineRequest));

        GoodsReceipt receipt = new GoodsReceipt();
        receipt.setId(receiptId);
        receipt.setWarehouseId(warehouseId);

        GoodsReceiptLine line = new GoodsReceiptLine();
        line.setSkuId(skuId);

        when(warehouseClient.getWarehouse(warehouseId))
                .thenReturn(warehouseResponse);
        when(goodsReceiptMapper.toEntity(request))
                .thenReturn(receipt);
        when(goodsReceiptRepository.save(any(GoodsReceipt.class)))
                .thenAnswer(invocation -> {
                    GoodsReceipt saved = invocation.getArgument(0);
                    saved.setId(receiptId);
                    return saved;
                });
        when(goodsReceiptMapper.toLineEntity(lineRequest))
                .thenReturn(line);
        when(goodsReceiptLineRepository.saveAll(anyList()))
                .thenThrow(new RuntimeException("Database failure"));

        assertThrows(
                RuntimeException.class,
                () -> goodsReceiptService.createGoodsReceipt(
                        request, correlationId)
        );

        verify(outboxEventService, never())
                .createGoodsReceivedEvent(
                        any(GoodsReceipt.class),
                        anyList(),
                        eq(correlationId));
    }
}
