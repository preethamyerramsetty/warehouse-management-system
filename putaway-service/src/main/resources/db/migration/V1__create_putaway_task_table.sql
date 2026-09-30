CREATE TABLE putaway_task (
    id UUID PRIMARY KEY,
    task_number VARCHAR(50) NOT NULL,
    goods_receipt_line_id UUID NOT NULL,
    sku_id UUID NOT NULL,
    quantity NUMERIC(15,2) NOT NULL,
    target_bin_id UUID NOT NULL,
    status VARCHAR(20) NOT NULL,
    assigned_to VARCHAR(100),
    created_at TIMESTAMP NOT NULL,
    completed_at TIMESTAMP,

    CONSTRAINT uk_putaway_task_number
        UNIQUE (task_number),

    CONSTRAINT chk_putaway_task_quantity
        CHECK (quantity > 0)
);