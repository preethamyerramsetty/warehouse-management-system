package com.company.wms.warehouse_service.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.company.wms.warehouse_service.domain.Warehouse;

public interface WarehouseRepository extends JpaRepository<Warehouse, UUID> {

    boolean existsByWarehouseCode(String warehouseCode);
}