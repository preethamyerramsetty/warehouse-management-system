package com.company.wms.inventory_service.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.company.wms.inventory_service.domain.OutboxEvent;
import com.company.wms.inventory_service.domain.OutboxEventStatus;

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