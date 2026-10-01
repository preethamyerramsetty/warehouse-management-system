package com.company.wms.putaway_service.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.company.wms.putaway_service.domain.ProcessedEvent;

public interface ProcessedEventRepository
        extends JpaRepository<ProcessedEvent, UUID> {

    boolean existsByEventId(UUID eventId);
}