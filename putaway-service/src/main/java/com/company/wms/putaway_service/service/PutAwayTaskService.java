package com.company.wms.putaway_service.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.company.wms.putaway_service.domain.PutAwayReceiptLine;
import com.company.wms.putaway_service.domain.PutAwayTask;
import com.company.wms.putaway_service.domain.PutAwayTaskStatus;
import com.company.wms.putaway_service.dto.CreatePutAwayTaskRequest;
import com.company.wms.putaway_service.dto.PutAwayTaskResponse;
import com.company.wms.putaway_service.exception.InvalidOperationException;
import com.company.wms.putaway_service.exception.ResourceNotFoundException;
import com.company.wms.putaway_service.mapper.PutAwayTaskMapper;
import com.company.wms.putaway_service.repository.PutAwayReceiptLineRepository;
import com.company.wms.putaway_service.repository.PutAwayTaskRepository;


@Service
public class PutAwayTaskService {

    private final PutAwayTaskRepository putAwayTaskRepository;
    private final PutAwayTaskMapper putAwayTaskMapper;
    private final PutAwayReceiptLineRepository putAwayReceiptLineRepository;
    private final OutboxEventService outboxEventService;

    public PutAwayTaskService(
        PutAwayTaskRepository putAwayTaskRepository,
        PutAwayTaskMapper putAwayTaskMapper,
        PutAwayReceiptLineRepository putAwayReceiptLineRepository,
        OutboxEventService outboxEventService) {

        this.putAwayTaskRepository = putAwayTaskRepository;
        this.putAwayTaskMapper = putAwayTaskMapper;
        this.putAwayReceiptLineRepository = putAwayReceiptLineRepository;
        this.outboxEventService = outboxEventService;
 }

    @Transactional
        public PutAwayTaskResponse createPutAwayTask(
                CreatePutAwayTaskRequest request) {

        PutAwayReceiptLine receiptLine =
                putAwayReceiptLineRepository
                        .findByGoodsReceiptLineId(
                                request.getGoodsReceiptLineId())
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Goods Receipt Line is not available "
                                                + "for Put-Away: "
                                                + request.getGoodsReceiptLineId()));

        if (request.getQuantity()
                .compareTo(receiptLine.getRemainingQuantity()) > 0) {

                throw new InvalidOperationException(
                        "Put-Away quantity "
                                + request.getQuantity()
                                + " exceeds remaining quantity "
                                + receiptLine.getRemainingQuantity());
        }

        BigDecimal newPutAwayQuantity =
                receiptLine.getPutawayQuantity()
                        .add(request.getQuantity());

        BigDecimal newRemainingQuantity =
                receiptLine.getRemainingQuantity()
                        .subtract(request.getQuantity());

        receiptLine.setPutawayQuantity(
                newPutAwayQuantity);

        receiptLine.setRemainingQuantity(
                newRemainingQuantity);

        receiptLine.setUpdatedAt(
                LocalDateTime.now());

        putAwayReceiptLineRepository.save(receiptLine);

        PutAwayTask task =
                putAwayTaskMapper.toEntity(request);

        task.setTaskNumber(generateTaskNumber());

        task.setCreatedAt(LocalDateTime.now());

        task.setStatus(PutAwayTaskStatus.COMPLETED);
        task.setCompletedAt(LocalDateTime.now());

        PutAwayTask savedTask =
                putAwayTaskRepository.save(task);

        outboxEventService.createPutAwayCompletedEvent(
                savedTask);

        return putAwayTaskMapper.toResponse(savedTask);
  }

    @Transactional(readOnly = true)
    public PutAwayTaskResponse getPutAwayTask(UUID id) {

        PutAwayTask task =
                putAwayTaskRepository.findById(id)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Put-Away task not found with id: " + id));

        return putAwayTaskMapper.toResponse(task);
    }

    @Transactional
    public PutAwayTaskResponse completePutAwayTask(UUID id) {

        PutAwayTask task =
                putAwayTaskRepository.findById(id)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Put-Away task not found with id: " + id));

        if (task.getStatus() == PutAwayTaskStatus.COMPLETED) {
            throw new InvalidOperationException(
                    "Put-Away task is already completed");
        }

        if (task.getStatus() == PutAwayTaskStatus.CANCELLED) {
            throw new InvalidOperationException(
                    "Cancelled Put-Away task cannot be completed");
        }

        task.setStatus(PutAwayTaskStatus.COMPLETED);

        task.setCompletedAt(LocalDateTime.now());

        PutAwayTask savedTask =
                putAwayTaskRepository.save(task);

        return putAwayTaskMapper.toResponse(savedTask);
    }

    @Transactional
        public PutAwayTaskResponse createPutAwayTaskFromGoodsReceived(
                UUID goodsReceiptLineId,
                UUID skuId,
                java.math.BigDecimal quantity) {

        PutAwayTask task = new PutAwayTask();

        task.setGoodsReceiptLineId(goodsReceiptLineId);
        task.setSkuId(skuId);
        task.setQuantity(quantity);

        /*
        * Target bin selection will be implemented later.
        * For now the task is created without a target bin.
        */
        task.setTargetBinId(null);

        task.setTaskNumber(generateTaskNumber());
        task.setStatus(PutAwayTaskStatus.OPEN);
        task.setCreatedAt(LocalDateTime.now());
        task.setCompletedAt(null);

        PutAwayTask savedTask =
                putAwayTaskRepository.save(task);

        return putAwayTaskMapper.toResponse(savedTask);
        }

    private String generateTaskNumber() {

        return "PT-" +
                UUID.randomUUID()
                        .toString()
                        .substring(0, 8)
                        .toUpperCase();
    }
}