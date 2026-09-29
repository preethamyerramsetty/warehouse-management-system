package com.company.wms.inbound_service.service;

import com.company.wms.inbound_service.domain.OutboxEvent;
import com.company.wms.inbound_service.domain.OutboxEventStatus;
import com.company.wms.inbound_service.repository.OutboxEventRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
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
                                OutboxEventStatus.PENDING);

        for (OutboxEvent event : events) {

            try {

                kafkaEventPublisher
                        .publish(event)
                        .join();

                event.setStatus(
                        OutboxEventStatus.PUBLISHED);

                event.setPublishedAt(
                        LocalDateTime.now());

                event.setLastError(null);

                outboxEventRepository.save(event);

            } catch (Exception exception) {

                event.setRetryCount(
                        event.getRetryCount() + 1);

                event.setLastError(
                        exception.getMessage());

                outboxEventRepository.save(event);
            }
        }
    }
}