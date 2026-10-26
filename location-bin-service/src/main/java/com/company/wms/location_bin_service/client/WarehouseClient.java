package com.company.wms.location_bin_service.client;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;

import com.company.wms.location_bin_service.exception.ResourceNotFoundException;

@Component
public class WarehouseClient {

    private final RestClient restClient;

    public WarehouseClient(
            RestClient.Builder builder,
            @Value("${services.warehouse.base-url}") String baseUrl,
            @Value("${services.warehouse.username}") String username,
            @Value("${services.warehouse.password}") String password) {

        this.restClient = builder
                .baseUrl(baseUrl)
                .defaultHeaders(headers ->
                        headers.setBasicAuth(username, password))
                .build();
    }

    public void validateWarehouseExists(UUID warehouseId) {
        try {
            restClient.get()
                    .uri("/api/v1/warehouses/{id}", warehouseId)
                    .retrieve()
                    .toBodilessEntity();

        } catch (HttpClientErrorException.NotFound ex) {
            throw new ResourceNotFoundException(
                    "Warehouse not found: " + warehouseId);
        }
    }
}