CREATE TABLE warehouse (
    id UUID PRIMARY KEY,
    warehouse_code VARCHAR(50) NOT NULL,
    name VARCHAR(100) NOT NULL,
    address VARCHAR(255),
    status VARCHAR(20) NOT NULL,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL,

    CONSTRAINT uk_warehouse_code
        UNIQUE (warehouse_code)
);