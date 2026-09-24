CREATE TABLE location (
    id UUID PRIMARY KEY,
    warehouse_id UUID NOT NULL,
    location_code VARCHAR(50) NOT NULL,
    name VARCHAR(100) NOT NULL,
    type VARCHAR(30) NOT NULL,
    status VARCHAR(20) NOT NULL,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL,

    CONSTRAINT uk_location_warehouse_code
        UNIQUE (warehouse_id, location_code)
);