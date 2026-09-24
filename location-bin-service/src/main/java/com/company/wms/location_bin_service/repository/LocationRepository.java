package com.company.wms.location_bin_service.repository;

import com.company.wms.location_bin_service.domain.Location;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface LocationRepository extends JpaRepository<Location, UUID> {

    boolean existsByWarehouseIdAndLocationCode(
            UUID warehouseId,
            String locationCode
    );
}