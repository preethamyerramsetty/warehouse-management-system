package com.company.wms.inventory_service.service;

import com.company.wms.inventory_service.domain.OutboxEvent;
import com.company.wms.inventory_service.domain.OutboxEventStatus;
import com.company.wms.inventory_service.repository.OutboxEventRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
public class OutboxPublisher {

    private final OutboxEventRepository outboxEventRepository;
    private final KafkaEventPublisher kafkaEventPublisher;

    public OutboxPublisher(
            OutboxEventRepository outboxEventRepository,
            KafkaEventPublisher kafkaEventPublisher) {

        this.outboxEventRepository = outboxEventRepository;
        this.kafkaEventPublisher = kafkaEventPublisher;
    }

    @Scheduled(fixedDelay = 5000)
    public void publishPendingEvents() {

        List<OutboxEvent> events =
                outboxEventRepository
                        .findByStatusOrderByCreatedAtAsc(
                                OutboxEventStatus.PENDING
                        );

        for (OutboxEvent event : events) {

            try {

                kafkaEventPublisher
                        .publish(event)
                        .join();

                event.setStatus(
                        OutboxEventStatus.PUBLISHED
                );

                event.setPublishedAt(
                        LocalDateTime.now()
                );

                event.setLastError(null);

                outboxEventRepository.save(event);

            } catch (Exception e) {

                event.setRetryCount(
                        event.getRetryCount() + 1
                );

                event.setLastError(
                        e.getMessage()
                );

                outboxEventRepository.save(event);
            }
        }
    }
}