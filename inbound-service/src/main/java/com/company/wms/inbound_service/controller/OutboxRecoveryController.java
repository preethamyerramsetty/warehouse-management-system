package com.company.wms.inbound_service.controller;

import com.company.wms.inbound_service.service.OutboxRecoveryService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/internal/outbox")
public class OutboxRecoveryController {

    private final OutboxRecoveryService outboxRecoveryService;

    public OutboxRecoveryController(
            OutboxRecoveryService outboxRecoveryService) {

        this.outboxRecoveryService = outboxRecoveryService;
    }

    @PostMapping("/{eventId}/retry")
    public ResponseEntity<Void> retryFailedEvent(
            @PathVariable UUID eventId) {

        outboxRecoveryService.retryFailedEvent(eventId);

        return ResponseEntity.accepted().build();
    }
}