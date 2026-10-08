package com.company.wms.putaway_service.service;

import com.company.wms.putaway_service.domain.OutboxEvent;
import com.company.wms.putaway_service.domain.OutboxEventStatus;
import com.company.wms.putaway_service.repository.OutboxEventRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class OutboxRecoveryService {

    private final OutboxEventRepository outboxEventRepository;

    public OutboxRecoveryService(
            OutboxEventRepository outboxEventRepository) {

        this.outboxEventRepository = outboxEventRepository;
    }

    @Transactional
    public void retryFailedEvent(UUID eventId) {

        OutboxEvent event =
                outboxEventRepository
                        .findByIdAndStatus(
                                eventId,
                                OutboxEventStatus.FAILED
                        )
                        .orElseThrow(() ->
                                new IllegalArgumentException(
                                        "Failed outbox event not found: " + eventId
                                )
                        );

        event.setStatus(OutboxEventStatus.PENDING);
        event.setRetryCount(0);
        event.setNextAttemptAt(LocalDateTime.now());
        event.setLastError(null);

        outboxEventRepository.save(event);
    }
}