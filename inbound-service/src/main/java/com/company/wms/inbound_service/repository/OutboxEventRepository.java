package com.company.wms.inbound_service.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.company.wms.inbound_service.domain.OutboxEvent;
import com.company.wms.inbound_service.domain.OutboxEventStatus;

public interface OutboxEventRepository
        extends JpaRepository<OutboxEvent, UUID> {

    List<OutboxEvent> findByStatusAndNextAttemptAtLessThanEqualOrderByCreatedAtAsc(
            OutboxEventStatus status,
            LocalDateTime now
    );

    Optional<OutboxEvent> findByIdAndStatus(
            UUID id,
            OutboxEventStatus status
    );
}