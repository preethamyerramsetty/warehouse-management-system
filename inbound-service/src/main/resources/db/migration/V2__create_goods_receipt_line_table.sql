CREATE TABLE goods_receipt_line (
    id UUID PRIMARY KEY,
    goods_receipt_id UUID NOT NULL,
    sku_id UUID NOT NULL,
    expected_quantity NUMERIC(15,2) NOT NULL,
    received_quantity NUMERIC(15,2) NOT NULL,
    uom VARCHAR(20) NOT NULL,
    batch_number VARCHAR(100),
    serial_number VARCHAR(100),
    status VARCHAR(30) NOT NULL,

    CONSTRAINT fk_goods_receipt_line_receipt
        FOREIGN KEY (goods_receipt_id)
        REFERENCES goods_receipt(id)
);