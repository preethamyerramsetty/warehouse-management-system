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

import com.company.wms.location_bin_service.dto.BinResponse;
import com.company.wms.location_bin_service.dto.CreateBinRequest;
import com.company.wms.location_bin_service.service.BinService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1")
public class BinController {

    private final BinService binService;

    public BinController(BinService binService) {
        this.binService = binService;
    }

    @PostMapping("/bins")
    public ResponseEntity<BinResponse> createBin(
            @Valid @RequestBody CreateBinRequest request) {

        BinResponse response = binService.createBin(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @GetMapping("/bins/{id}")
    public ResponseEntity<BinResponse> getBin(
            @PathVariable UUID id) {

        BinResponse response = binService.getBin(id);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/locations/{locationId}/bins")
    public ResponseEntity<List<BinResponse>> getBinsByLocation(
            @PathVariable UUID locationId) {

        List<BinResponse> response =
                binService.getBinsByLocation(locationId);

        return ResponseEntity.ok(response);
    }
}