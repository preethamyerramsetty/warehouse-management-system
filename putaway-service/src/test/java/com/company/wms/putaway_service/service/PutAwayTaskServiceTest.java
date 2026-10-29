
package com.company.wms.putaway_service.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;

import com.company.wms.putaway_service.client.LocationBinClient;
import com.company.wms.putaway_service.domain.PutAwayReceiptLine;
import com.company.wms.putaway_service.domain.PutAwayTask;
import com.company.wms.putaway_service.domain.PutAwayTaskStatus;
import com.company.wms.putaway_service.dto.BinResponse;
import com.company.wms.putaway_service.dto.CreatePutAwayTaskRequest;
import com.company.wms.putaway_service.dto.LocationResponse;
import com.company.wms.putaway_service.exception.InvalidOperationException;
import com.company.wms.putaway_service.mapper.PutAwayTaskMapper;
import com.company.wms.putaway_service.repository.PutAwayReceiptLineRepository;
import com.company.wms.putaway_service.repository.PutAwayTaskRepository;

@ExtendWith(MockitoExtension.class)
class PutAwayTaskServiceTest {

    @Mock
    private PutAwayTaskRepository putAwayTaskRepository;

    @Mock
    private PutAwayTaskMapper putAwayTaskMapper;

    @Mock
    private PutAwayReceiptLineRepository putAwayReceiptLineRepository;

    @Mock
    private OutboxEventService outboxEventService;

    @Mock
    private LocationBinClient locationBinClient;

    @InjectMocks
    private PutAwayTaskService putAwayTaskService;

    private UUID taskId;
    private UUID receiptLineId;
    private UUID skuId;
    private UUID warehouseId;
    private UUID binId;
    private UUID locationId;

    @BeforeEach
    void setUp() {
        taskId = UUID.randomUUID();
        receiptLineId = UUID.randomUUID();
        skuId = UUID.randomUUID();
        warehouseId = UUID.randomUUID();
        binId = UUID.randomUUID();
        locationId = UUID.randomUUID();
    }

    @Test
    void completePutAwayTask_whenAlreadyCompleted_throwsException() {
        PutAwayTask task = new PutAwayTask();
        task.setId(taskId);
        task.setStatus(PutAwayTaskStatus.COMPLETED);

        when(putAwayTaskRepository.findById(taskId))
                .thenReturn(Optional.of(task));

        assertThrows(
                InvalidOperationException.class,
                () -> putAwayTaskService.completePutAwayTask(taskId)
        );

        verify(putAwayTaskRepository, never()).save(task);
        verify(outboxEventService, never())
                .createPutAwayCompletedEvent(task);
    }

    @Test
    void createPutAwayTask_whenQuantityExceedsRemainingQuantity_throwsException() {
        CreatePutAwayTaskRequest request = new CreatePutAwayTaskRequest();
        request.setGoodsReceiptLineId(receiptLineId);
        request.setSkuId(skuId);
        request.setWarehouseId(warehouseId);
        request.setTargetBinId(binId);
        request.setQuantity(new BigDecimal("6.00"));

        BinResponse bin = new BinResponse(
                binId,
                locationId,
                "BIN-01",
                new BigDecimal("100.00"),
                "ACTIVE"
        );

        LocationResponse location = new LocationResponse(
                locationId,
                warehouseId,
                "LOC-01",
                "Storage Location",
                "STORAGE",
                "ACTIVE"
        );

        PutAwayReceiptLine receiptLine = new PutAwayReceiptLine();
        receiptLine.setId(UUID.randomUUID());
        receiptLine.setGoodsReceiptLineId(receiptLineId);
        receiptLine.setSkuId(skuId);
        receiptLine.setReceivedQuantity(new BigDecimal("10.00"));
        receiptLine.setPutawayQuantity(new BigDecimal("5.00"));
        receiptLine.setRemainingQuantity(new BigDecimal("5.00"));
        receiptLine.setCreatedAt(LocalDateTime.now());
        receiptLine.setUpdatedAt(LocalDateTime.now());

        when(putAwayReceiptLineRepository.findByGoodsReceiptLineId(receiptLineId))
                .thenReturn(Optional.of(receiptLine));

        assertThrows(
                InvalidOperationException.class,
                () -> putAwayTaskService.createPutAwayTask(request)
        );

        verify(putAwayReceiptLineRepository, never()).save(receiptLine);
        verify(putAwayTaskRepository, never()).save(
                org.mockito.ArgumentMatchers.any(PutAwayTask.class)
        );
    }
}
