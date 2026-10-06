CREATE TABLE inventory_balance (
    id UUID PRIMARY KEY,
    sku_id UUID NOT NULL,
    warehouse_id UUID NOT NULL,
    bin_id UUID NOT NULL,
    available_quantity NUMERIC(15,2) NOT NULL DEFAULT 0,
    reserved_quantity NUMERIC(15,2) NOT NULL DEFAULT 0,
    version BIGINT NOT NULL DEFAULT 0,
    updated_at TIMESTAMP NOT NULL,

    CONSTRAINT uk_inventory_balance_sku_warehouse_bin
        UNIQUE (sku_id, warehouse_id, bin_id),

    CONSTRAINT chk_inventory_available_quantity
        CHECK (available_quantity >= 0),

    CONSTRAINT chk_inventory_reserved_quantity
        CHECK (reserved_quantity >= 0)
);

CREATE TABLE inventory_movement (
    id UUID PRIMARY KEY,
    sku_id UUID NOT NULL,
    warehouse_id UUID NOT NULL,
    bin_id UUID NOT NULL,
    movement_type VARCHAR(30) NOT NULL,
    quantity NUMERIC(15,2) NOT NULL,
    reference_type VARCHAR(50) NOT NULL,
    reference_id VARCHAR(100) NOT NULL,
    event_id UUID NOT NULL,
    created_at TIMESTAMP NOT NULL,

    CONSTRAINT uk_inventory_movement_event_id
        UNIQUE (event_id),

    CONSTRAINT chk_inventory_movement_quantity
        CHECK (quantity > 0)
);

CREATE INDEX idx_inventory_balance_sku
    ON inventory_balance(sku_id);

CREATE INDEX idx_inventory_balance_warehouse
    ON inventory_balance(warehouse_id);

CREATE INDEX idx_inventory_balance_bin
    ON inventory_balance(bin_id);

CREATE INDEX idx_inventory_movement_sku
    ON inventory_movement(sku_id);

CREATE INDEX idx_inventory_movement_warehouse
    ON inventory_movement(warehouse_id);

CREATE INDEX idx_inventory_movement_bin
    ON inventory_movement(bin_id);