package com.company.wms.inventory_service.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.company.wms.inventory_service.domain.OutboxEvent;
import com.company.wms.inventory_service.domain.OutboxEventStatus;
import com.company.wms.inventory_service.repository.OutboxEventRepository;

@Component
public class OutboxPublisher {

    private static final int MAX_RETRIES = 5;

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

        LocalDateTime now = LocalDateTime.now();

        List<OutboxEvent> events =
                outboxEventRepository
                        .findByStatusAndNextAttemptAtLessThanEqualOrderByCreatedAtAsc(
                                OutboxEventStatus.PENDING,
                                now
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
                event.setNextAttemptAt(null);

                outboxEventRepository.save(event);

            } catch (Exception e) {

                int retryCount =
                        event.getRetryCount() + 1;

                event.setRetryCount(retryCount);

                event.setLastError(
                        e.getMessage()
                );

                if (retryCount >= MAX_RETRIES) {

                    event.setStatus(
                            OutboxEventStatus.FAILED
                    );

                    event.setNextAttemptAt(null);

                } else {

                    long delaySeconds =
                            (long) Math.pow(2, retryCount);

                    event.setNextAttemptAt(
                            LocalDateTime.now()
                                    .plusSeconds(delaySeconds)
                    );
                }

                outboxEventRepository.save(event);
            }
        }
    }
}