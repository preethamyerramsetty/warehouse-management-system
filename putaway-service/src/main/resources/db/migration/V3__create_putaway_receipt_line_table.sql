CREATE TABLE putaway_receipt_line (
    id UUID PRIMARY KEY,
    goods_receipt_line_id UUID NOT NULL,
    sku_id UUID NOT NULL,
    received_quantity NUMERIC(15,2) NOT NULL,
    putaway_quantity NUMERIC(15,2) NOT NULL,
    remaining_quantity NUMERIC(15,2) NOT NULL,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL,

    CONSTRAINT uk_putaway_receipt_line
        UNIQUE (goods_receipt_line_id),

    CONSTRAINT chk_received_quantity
        CHECK (received_quantity > 0),

    CONSTRAINT chk_putaway_quantity
        CHECK (putaway_quantity >= 0),

    CONSTRAINT chk_remaining_quantity
        CHECK (remaining_quantity >= 0)
);

CREATE INDEX idx_putaway_receipt_line_sku
ON putaway_receipt_line(sku_id);