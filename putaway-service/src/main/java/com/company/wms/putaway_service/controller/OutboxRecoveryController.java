package com.company.wms.putaway_service.controller;

import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.company.wms.putaway_service.service.OutboxRecoveryService;

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