package com.company.wms.location_bin_service.mapper;

import org.springframework.stereotype.Component;

import com.company.wms.location_bin_service.domain.Location;
import com.company.wms.location_bin_service.dto.CreateLocationRequest;
import com.company.wms.location_bin_service.dto.LocationResponse;

@Component
public class LocationMapper {

    public Location toEntity(CreateLocationRequest request) {

        Location location = new Location();

        location.setWarehouseId(request.getWarehouseId());
        location.setLocationCode(request.getLocationCode());
        location.setName(request.getName());
        location.setType(request.getType());
        location.setStatus(request.getStatus());

        return location;
    }

    public LocationResponse toResponse(Location location) {

        LocationResponse response = new LocationResponse();

        response.setId(location.getId());
        response.setWarehouseId(location.getWarehouseId());
        response.setLocationCode(location.getLocationCode());
        response.setName(location.getName());
        response.setType(location.getType());
        response.setStatus(location.getStatus());
        response.setCreatedAt(location.getCreatedAt());
        response.setUpdatedAt(location.getUpdatedAt());

        return response;
    }
}