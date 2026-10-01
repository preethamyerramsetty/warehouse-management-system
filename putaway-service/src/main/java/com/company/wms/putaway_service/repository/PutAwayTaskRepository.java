package com.company.wms.putaway_service.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.company.wms.putaway_service.domain.PutAwayTask;

public interface PutAwayTaskRepository
        extends JpaRepository<PutAwayTask, UUID> {

    boolean existsByTaskNumber(String taskNumber);
}