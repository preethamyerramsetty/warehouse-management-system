CREATE TABLE outbox_event (
    id UUID PRIMARY KEY,
    event_id UUID NOT NULL,
    event_type VARCHAR(100) NOT NULL,
    event_version INTEGER NOT NULL,
    occurred_at TIMESTAMP NOT NULL,
    source VARCHAR(100) NOT NULL,
    correlation_id VARCHAR(100),
    entity_id VARCHAR(100) NOT NULL,
    payload TEXT NOT NULL,
    status VARCHAR(20) NOT NULL,
    retry_count INTEGER NOT NULL DEFAULT 0,
    created_at TIMESTAMP NOT NULL,
    published_at TIMESTAMP,
    next_attempt_at TIMESTAMP,
    last_error TEXT,

    CONSTRAINT uk_outbox_event_event_id UNIQUE (event_id)
);

CREATE INDEX idx_outbox_event_status_created
ON outbox_event(status, created_at);

CREATE INDEX idx_outbox_event_event_type
ON outbox_event(event_type);

CREATE INDEX idx_outbox_event_entity_id
ON outbox_event(entity_id);