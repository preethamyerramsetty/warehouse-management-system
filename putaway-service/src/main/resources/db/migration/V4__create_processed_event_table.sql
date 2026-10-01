CREATE TABLE processed_event (
    id UUID PRIMARY KEY,
    event_id UUID NOT NULL,
    event_type VARCHAR(100) NOT NULL,
    processed_at TIMESTAMP NOT NULL,

    CONSTRAINT uk_processed_event_event_id
        UNIQUE (event_id)
);

CREATE INDEX idx_processed_event_type
ON processed_event(event_type);