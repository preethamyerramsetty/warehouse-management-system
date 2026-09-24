package com.company.wms.warehouse_service.service;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.company.wms.warehouse_service.domain.Warehouse;
import com.company.wms.warehouse_service.dto.CreateWarehouseRequest;
import com.company.wms.warehouse_service.dto.WarehouseResponse;
import com.company.wms.warehouse_service.exception.DuplicateResourceException;
import com.company.wms.warehouse_service.exception.ResourceNotFoundException;
import com.company.wms.warehouse_service.mapper.WarehouseMapper;
import com.company.wms.warehouse_service.repository.WarehouseRepository;

@Service
public class WarehouseService {

    private final WarehouseRepository warehouseRepository;
    private final WarehouseMapper warehouseMapper;

    public WarehouseService(
            WarehouseRepository warehouseRepository,
            WarehouseMapper warehouseMapper) {

        this.warehouseRepository = warehouseRepository;
        this.warehouseMapper = warehouseMapper;
    }

    @Transactional
    public WarehouseResponse createWarehouse(CreateWarehouseRequest request) {

        if (warehouseRepository.existsByWarehouseCode(
                request.getWarehouseCode())) {

            throw new DuplicateResourceException(
                    "Warehouse code already exists: "
                            + request.getWarehouseCode()
            );
        }

        Warehouse warehouse = warehouseMapper.toEntity(request);

        LocalDateTime now = LocalDateTime.now();

        warehouse.setCreatedAt(now);
        warehouse.setUpdatedAt(now);

        Warehouse savedWarehouse =
                warehouseRepository.save(warehouse);

        return warehouseMapper.toResponse(savedWarehouse);
    }

    @Transactional(readOnly = true)
    public WarehouseResponse getWarehouse(UUID id) {

        Warehouse warehouse = warehouseRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Warehouse not found: " + id
                        ));

        return warehouseMapper.toResponse(warehouse);
    }
}