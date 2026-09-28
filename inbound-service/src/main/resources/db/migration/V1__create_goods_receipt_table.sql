CREATE TABLE goods_receipt (
    id UUID PRIMARY KEY,
    receipt_number VARCHAR(50) NOT NULL,
    warehouse_id UUID NOT NULL,
    reference_type VARCHAR(50) NOT NULL,
    reference_id VARCHAR(100) NOT NULL,
    status VARCHAR(30) NOT NULL,
    received_at TIMESTAMP,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL,

    CONSTRAINT uk_goods_receipt_number
        UNIQUE (receipt_number)
);