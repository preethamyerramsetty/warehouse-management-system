package com.company.wms.warehouse_service.mapper;

import org.springframework.stereotype.Component;

import com.company.wms.warehouse_service.domain.Warehouse;
import com.company.wms.warehouse_service.dto.CreateWarehouseRequest;
import com.company.wms.warehouse_service.dto.WarehouseResponse;

@Component
public class WarehouseMapper {

    public Warehouse toEntity(CreateWarehouseRequest request) {

        Warehouse warehouse = new Warehouse();

        warehouse.setWarehouseCode(request.getWarehouseCode());
        warehouse.setName(request.getName());
        warehouse.setAddress(request.getAddress());
        warehouse.setStatus(request.getStatus());

        return warehouse;
    }

    public WarehouseResponse toResponse(Warehouse warehouse) {

        WarehouseResponse response = new WarehouseResponse();

        response.setId(warehouse.getId());
        response.setWarehouseCode(warehouse.getWarehouseCode());
        response.setName(warehouse.getName());
        response.setAddress(warehouse.getAddress());
        response.setStatus(warehouse.getStatus());
        response.setCreatedAt(warehouse.getCreatedAt());
        response.setUpdatedAt(warehouse.getUpdatedAt());

        return response;
    }
}