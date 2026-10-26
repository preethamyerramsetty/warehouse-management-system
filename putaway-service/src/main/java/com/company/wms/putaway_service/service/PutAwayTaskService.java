package com.company.wms.putaway_service.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestClientResponseException;

import com.company.wms.putaway_service.client.LocationBinClient;
import com.company.wms.putaway_service.domain.PutAwayReceiptLine;
import com.company.wms.putaway_service.domain.PutAwayTask;
import com.company.wms.putaway_service.domain.PutAwayTaskStatus;
import com.company.wms.putaway_service.dto.BinResponse;
import com.company.wms.putaway_service.dto.CreatePutAwayTaskRequest;
import com.company.wms.putaway_service.dto.LocationResponse;
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
    private final LocationBinClient locationBinClient;

    public PutAwayTaskService(
                PutAwayTaskRepository putAwayTaskRepository,
                PutAwayTaskMapper putAwayTaskMapper,
                PutAwayReceiptLineRepository putAwayReceiptLineRepository,
                OutboxEventService outboxEventService,
                LocationBinClient locationBinClient) {

        this.putAwayTaskRepository = putAwayTaskRepository;
        this.putAwayTaskMapper = putAwayTaskMapper;
        this.putAwayReceiptLineRepository = putAwayReceiptLineRepository;
        this.outboxEventService = outboxEventService;
        this.locationBinClient = locationBinClient;
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

        // Validate that the requested SKU matches the receipt line.
        if (!request.getSkuId().equals(receiptLine.getSkuId())) {
        throw new InvalidOperationException(
                "Requested SKU does not match the Goods Receipt Line.");
        }

        // Do not allow Put-Away for legacy receipt lines whose warehouse
        // cannot be determined.
        if (receiptLine.getWarehouseId() == null) {
        throw new InvalidOperationException(
                "Warehouse information is missing for this Goods Receipt Line.");
        }

        // Validate that the requested warehouse matches the receipt line.
        if (!request.getWarehouseId().equals(receiptLine.getWarehouseId())) {
        throw new InvalidOperationException(
                "Requested warehouse does not match the Goods Receipt Line.");
        }

        // Validate the target bin against the receipt line's warehouse,
        // not against an untrusted warehouse ID from the request.
        validateTargetBin(
                request.getTargetBinId(),
                receiptLine.getWarehouseId());

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

        task.setStatus(PutAwayTaskStatus.OPEN);
        task.setCompletedAt(null);

        task.setCreatedBy(
        org.springframework.security.core.context.SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName()
        );

        PutAwayTask savedTask =
                putAwayTaskRepository.save(task);

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

        task.setCompletedBy(
        org.springframework.security.core.context.SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName()
        );

        task.setCompletedAt(LocalDateTime.now());

        PutAwayTask savedTask =
                putAwayTaskRepository.save(task);

        outboxEventService.createPutAwayCompletedEvent(savedTask);

        return putAwayTaskMapper.toResponse(savedTask);
        }

    @Transactional
        public PutAwayTaskResponse createPutAwayTaskFromGoodsReceived(
                UUID goodsReceiptLineId,
                UUID skuId,
                BigDecimal quantity,
                UUID warehouseId) {

        PutAwayTask task = new PutAwayTask();

        task.setGoodsReceiptLineId(goodsReceiptLineId);
        task.setSkuId(skuId);
        task.setQuantity(quantity);
        task.setWarehouseId(warehouseId);

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

    private void validateTargetBin(UUID binId, UUID warehouseId) {

        BinResponse bin;

        try {
                bin = locationBinClient.getBin(binId);
        } catch (RestClientResponseException ex) {
                if (ex.getStatusCode().value() == 404) {
                throw new ResourceNotFoundException(
                        "Target bin not found: " + binId);
                }

                throw new InvalidOperationException(
                        "Unable to validate target bin. "
                                + "Location-bin service returned HTTP "
                                + ex.getStatusCode().value());
        }

        if (!"ACTIVE".equalsIgnoreCase(bin.status())) {
                throw new InvalidOperationException(
                        "Target bin is not active: " + binId);
        }

        LocationResponse location;

        try {
                location = locationBinClient.getLocation(bin.locationId());
        } catch (RestClientResponseException ex) {
                if (ex.getStatusCode().value() == 404) {
                throw new ResourceNotFoundException(
                        "Location not found for target bin: " + binId);
                }

                throw new InvalidOperationException(
                        "Unable to validate the target bin's location.");
        }

        if (!"ACTIVE".equalsIgnoreCase(location.status())) {
                throw new InvalidOperationException(
                        "Target bin's location is not active: "
                                + location.id());
        }

        if (!warehouseId.equals(location.warehouseId())) {
                throw new InvalidOperationException(
                        "Target bin does not belong to the specified warehouse.");
        }
        }
}