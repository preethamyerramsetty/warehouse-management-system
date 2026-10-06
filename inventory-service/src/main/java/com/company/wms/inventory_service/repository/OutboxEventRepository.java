package com.company.wms.inventory_service.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.company.wms.inventory_service.domain.OutboxEvent;
import com.company.wms.inventory_service.domain.OutboxEventStatus;

public interface OutboxEventRepository
        extends JpaRepository<OutboxEvent, UUID> {

    List<OutboxEvent> findByStatusOrderByCreatedAtAsc(
            OutboxEventStatus status
    );
}