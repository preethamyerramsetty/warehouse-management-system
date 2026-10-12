package com.company.wms.putaway_service.client;

import java.util.UUID;

import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import com.company.wms.putaway_service.dto.BinResponse;
import com.company.wms.putaway_service.dto.LocationResponse;

@Component
public class LocationBinClient {

    private final RestClient restClient;

    public LocationBinClient(RestClient locationBinRestClient) {
        this.restClient = locationBinRestClient;
    }

    public BinResponse getBin(UUID binId) {
        return restClient.get()
                .uri("/api/v1/bins/{id}", binId)
                .retrieve()
                .body(BinResponse.class);
    }

    public LocationResponse getLocation(UUID locationId) {
        return restClient.get()
                .uri("/api/v1/locations/{id}", locationId)
                .retrieve()
                .body(LocationResponse.class);
    }
}