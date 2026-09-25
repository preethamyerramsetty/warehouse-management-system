package com.company.wms.location_bin_service.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.company.wms.location_bin_service.dto.CreateLocationRequest;
import com.company.wms.location_bin_service.dto.LocationResponse;
import com.company.wms.location_bin_service.service.LocationService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1")
public class LocationController {

    private final LocationService locationService;

    public LocationController(LocationService locationService) {
        this.locationService = locationService;
    }

    @PostMapping("/locations")
    public ResponseEntity<LocationResponse> createLocation(
            @Valid @RequestBody CreateLocationRequest request) {

        LocationResponse response =
                locationService.createLocation(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @GetMapping("/locations/{id}")
    public ResponseEntity<LocationResponse> getLocation(
            @PathVariable UUID id) {

        LocationResponse response =
                locationService.getLocation(id);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/warehouses/{warehouseId}/locations")
    public ResponseEntity<List<LocationResponse>> getLocationsByWarehouse(
            @PathVariable UUID warehouseId) {

        List<LocationResponse> response =
                locationService.getLocationsByWarehouse(warehouseId);

        return ResponseEntity.ok(response);
    }
}