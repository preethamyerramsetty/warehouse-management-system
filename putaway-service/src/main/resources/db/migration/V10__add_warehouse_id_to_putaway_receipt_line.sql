ALTER TABLE putaway_receipt_line
ADD COLUMN warehouse_id UUID;
ALTER TABLE putaway_receipt_line
ADD COLUMN warehouse_id UUID;

-- Backfill existing receipt lines when their warehouse
-- can be determined unambiguously from existing tasks.
UPDATE putaway_receipt_line prl
SET warehouse_id = existing_tasks.warehouse_id
FROM (
    SELECT
        goods_receipt_line_id,
        MIN(warehouse_id::TEXT)::UUID AS warehouse_id
    FROM putaway_task
    WHERE warehouse_id IS NOT NULL
    GROUP BY goods_receipt_line_id
    HAVING COUNT(DISTINCT warehouse_id) = 1
) existing_tasks
WHERE prl.goods_receipt_line_id =
      existing_tasks.goods_receipt_line_id;