package com.company.wms.location_bin_service.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.company.wms.location_bin_service.domain.Location;
import com.company.wms.location_bin_service.dto.CreateLocationRequest;
import com.company.wms.location_bin_service.dto.LocationResponse;
import com.company.wms.location_bin_service.exception.DuplicateResourceException;
import com.company.wms.location_bin_service.exception.ResourceNotFoundException;
import com.company.wms.location_bin_service.mapper.LocationMapper;
import com.company.wms.location_bin_service.repository.LocationRepository;

@Service
public class LocationService {

    private final LocationRepository locationRepository;
    private final LocationMapper locationMapper;

    public LocationService(
            LocationRepository locationRepository,
            LocationMapper locationMapper) {

        this.locationRepository = locationRepository;
        this.locationMapper = locationMapper;
    }

    @Transactional
    public LocationResponse createLocation(CreateLocationRequest request) {

        if (locationRepository.existsByWarehouseIdAndLocationCode(
                request.getWarehouseId(),
                request.getLocationCode())) {

            throw new DuplicateResourceException(
                    "Location code already exists in warehouse: "
                            + request.getLocationCode()
            );
        }

        Location location = locationMapper.toEntity(request);

        LocalDateTime now = LocalDateTime.now();

        location.setCreatedAt(now);
        location.setUpdatedAt(now);

        Location savedLocation = locationRepository.save(location);

        return locationMapper.toResponse(savedLocation);
    }

    @Transactional(readOnly = true)
    public LocationResponse getLocation(UUID id) {

        Location location = locationRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Location not found: " + id
                        ));

        return locationMapper.toResponse(location);
    }

    @Transactional(readOnly = true)
    public List<LocationResponse> getLocationsByWarehouse(UUID warehouseId) {

        return locationRepository.findByWarehouseId(warehouseId)
                .stream()
                .map(locationMapper::toResponse)
                .toList();
    }
}