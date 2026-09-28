CREATE INDEX idx_goods_receipt_warehouse_id
ON goods_receipt(warehouse_id);

CREATE INDEX idx_goods_receipt_status
ON goods_receipt(status);

CREATE INDEX idx_goods_receipt_reference
ON goods_receipt(reference_type, reference_id);

CREATE INDEX idx_goods_receipt_line_receipt_id
ON goods_receipt_line(goods_receipt_id);

CREATE INDEX idx_goods_receipt_line_sku_id
ON goods_receipt_line(sku_id);

CREATE INDEX idx_goods_receipt_line_status
ON goods_receipt_line(status);