CREATE TABLE bin (
    id UUID PRIMARY KEY,
    location_id UUID NOT NULL,
    bin_code VARCHAR(50) NOT NULL,
    capacity NUMERIC(15,2) NOT NULL,
    status VARCHAR(20) NOT NULL,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL,

    CONSTRAINT uk_bin_location_code
        UNIQUE (location_id, bin_code)
);