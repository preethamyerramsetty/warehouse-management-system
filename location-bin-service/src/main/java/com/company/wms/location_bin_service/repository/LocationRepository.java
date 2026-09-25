package com.company.wms.location_bin_service.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.company.wms.location_bin_service.domain.Location;

public interface LocationRepository extends JpaRepository<Location, UUID> {

    boolean existsByWarehouseIdAndLocationCode(
            UUID warehouseId,
            String locationCode
    );

    List<Location> findByWarehouseId(UUID warehouseId);
}