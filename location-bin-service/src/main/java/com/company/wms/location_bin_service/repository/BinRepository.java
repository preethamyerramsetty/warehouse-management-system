package com.company.wms.location_bin_service.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.company.wms.location_bin_service.domain.Bin;

public interface BinRepository extends JpaRepository<Bin, UUID> {

    boolean existsByLocationIdAndBinCode(
            UUID locationId,
            String binCode
    );

    List<Bin> findByLocationId(UUID locationId);
}