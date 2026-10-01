package com.company.wms.putaway_service.controller;

import com.company.wms.putaway_service.dto.CreatePutAwayTaskRequest;
import com.company.wms.putaway_service.dto.PutAwayTaskResponse;
import com.company.wms.putaway_service.service.PutAwayTaskService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/putaway/tasks")
public class PutAwayTaskController {

    private final PutAwayTaskService putAwayTaskService;

    public PutAwayTaskController(
            PutAwayTaskService putAwayTaskService) {
        this.putAwayTaskService = putAwayTaskService;
    }

    @PostMapping
    public ResponseEntity<PutAwayTaskResponse> createPutAwayTask(
            @Valid @RequestBody CreatePutAwayTaskRequest request) {

        PutAwayTaskResponse response =
                putAwayTaskService.createPutAwayTask(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PutAwayTaskResponse> getPutAwayTask(
            @PathVariable UUID id) {

        PutAwayTaskResponse response =
                putAwayTaskService.getPutAwayTask(id);

        return ResponseEntity.ok(response);
    }

    @PostMapping("/{id}/complete")
    public ResponseEntity<PutAwayTaskResponse> completePutAwayTask(
            @PathVariable UUID id) {

        PutAwayTaskResponse response =
                putAwayTaskService.completePutAwayTask(id);

        return ResponseEntity.ok(response);
    }
}