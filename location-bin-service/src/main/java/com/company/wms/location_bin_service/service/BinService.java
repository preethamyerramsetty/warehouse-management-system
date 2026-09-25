package com.company.wms.location_bin_service.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.company.wms.location_bin_service.domain.Bin;
import com.company.wms.location_bin_service.dto.BinResponse;
import com.company.wms.location_bin_service.dto.CreateBinRequest;
import com.company.wms.location_bin_service.exception.DuplicateResourceException;
import com.company.wms.location_bin_service.exception.ResourceNotFoundException;
import com.company.wms.location_bin_service.mapper.BinMapper;
import com.company.wms.location_bin_service.repository.BinRepository;
import com.company.wms.location_bin_service.repository.LocationRepository;

@Service
public class BinService {

    private final BinRepository binRepository;
    private final LocationRepository locationRepository;
    private final BinMapper binMapper;

    public BinService(
            BinRepository binRepository,
            LocationRepository locationRepository,
            BinMapper binMapper) {

        this.binRepository = binRepository;
        this.locationRepository = locationRepository;
        this.binMapper = binMapper;
    }

    @Transactional
    public BinResponse createBin(CreateBinRequest request) {

        // 1. Check whether the Location exists
        if (!locationRepository.existsById(request.getLocationId())) {
            throw new ResourceNotFoundException(
                    "Location not found: " + request.getLocationId()
            );
        }

        // 2. Check for duplicate Bin code within the Location
        if (binRepository.existsByLocationIdAndBinCode(
                request.getLocationId(),
                request.getBinCode())) {

            throw new DuplicateResourceException(
                    "Bin code already exists in location: "
                            + request.getBinCode()
            );
        }

        // 3. Convert request to entity
        Bin bin = binMapper.toEntity(request);

        // 4. Set timestamps
        LocalDateTime now = LocalDateTime.now();

        bin.setCreatedAt(now);
        bin.setUpdatedAt(now);

        // 5. Save
        Bin savedBin = binRepository.save(bin);

        // 6. Convert entity to response
        return binMapper.toResponse(savedBin);
    }

    @Transactional(readOnly = true)
    public BinResponse getBin(UUID id) {

        Bin bin = binRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Bin not found: " + id
                        ));

        return binMapper.toResponse(bin);
    }

    @Transactional(readOnly = true)
    public List<BinResponse> getBinsByLocation(UUID locationId) {

        // Verify that the Location exists
        if (!locationRepository.existsById(locationId)) {
            throw new ResourceNotFoundException(
                    "Location not found: " + locationId
            );
        }

        return binRepository.findByLocationId(locationId)
                .stream()
                .map(binMapper::toResponse)
                .toList();
    }
}