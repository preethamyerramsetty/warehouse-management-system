package com.company.wms.putaway_service.repository;

import com.company.wms.putaway_service.domain.OutboxEvent;
import com.company.wms.putaway_service.domain.OutboxEventStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface OutboxEventRepository
        extends JpaRepository<OutboxEvent, UUID> {

    List<OutboxEvent>
    findByStatusOrderByCreatedAtAsc(
            OutboxEventStatus status);
}