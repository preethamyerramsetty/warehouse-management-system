package com.company.wms.inbound_service.client;

import java.util.UUID;

import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
public class WarehouseClient {

    private final RestClient restClient;

    public WarehouseClient(RestClient warehouseRestClient) {
        this.restClient = warehouseRestClient;
    }

    public WarehouseResponse getWarehouse(UUID warehouseId) {
        return restClient.get()
                .uri("/api/v1/warehouses/{id}", warehouseId)
                .retrieve()
                .body(WarehouseResponse.class);
    }
}