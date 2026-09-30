CREATE INDEX idx_putaway_task_receipt_line
ON putaway_task(goods_receipt_line_id);

CREATE INDEX idx_putaway_task_sku
ON putaway_task(sku_id);

CREATE INDEX idx_putaway_task_target_bin
ON putaway_task(target_bin_id);

CREATE INDEX idx_putaway_task_status
ON putaway_task(status);