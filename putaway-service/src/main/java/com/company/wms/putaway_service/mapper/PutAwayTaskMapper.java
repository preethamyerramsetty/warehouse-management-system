package com.company.wms.putaway_service.mapper;

import org.springframework.stereotype.Component;

import com.company.wms.putaway_service.domain.PutAwayTask;
import com.company.wms.putaway_service.dto.CreatePutAwayTaskRequest;
import com.company.wms.putaway_service.dto.PutAwayTaskResponse;

@Component
public class PutAwayTaskMapper {

    public PutAwayTask toEntity(
            CreatePutAwayTaskRequest request) {

        PutAwayTask task = new PutAwayTask();

        task.setGoodsReceiptLineId(
                request.getGoodsReceiptLineId());

        task.setSkuId(
                request.getSkuId());

        task.setWarehouseId(request.getWarehouseId());

        task.setQuantity(
                request.getQuantity());

        task.setTargetBinId(
                request.getTargetBinId());

        task.setAssignedTo(
                request.getAssignedTo());

        return task;
    }

    public PutAwayTaskResponse toResponse(
            PutAwayTask task) {

        PutAwayTaskResponse response =
                new PutAwayTaskResponse();

        response.setId(task.getId());
        response.setWarehouseId(task.getWarehouseId());
        response.setTaskNumber(task.getTaskNumber());
        response.setGoodsReceiptLineId(
                task.getGoodsReceiptLineId());
        response.setSkuId(task.getSkuId());
        response.setQuantity(task.getQuantity());
        response.setTargetBinId(task.getTargetBinId());
        response.setStatus(task.getStatus());
        response.setAssignedTo(task.getAssignedTo());
        response.setCreatedAt(task.getCreatedAt());
        response.setCompletedAt(task.getCompletedAt());

        return response;
    }
}