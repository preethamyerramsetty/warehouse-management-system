
package com.company.wms.inbound_service.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
public class WarehouseClientConfig {

    @Bean
    public RestClient warehouseRestClient(
            @Value("${wms.warehouse.base-url}") String baseUrl,
            @Value("${wms.gateway.base-url}") String gatewayUrl,
            @Value("${wms.gateway.username}") String username,
            @Value("${wms.gateway.password}") String password) {

        RestClient gatewayClient = RestClient.builder()
                .baseUrl(gatewayUrl)
                .build();

        String token = gatewayClient.post()
                .uri("/auth/token")
                .body(new TokenRequest(username, password))
                .retrieve()
                .body(TokenResponse.class)
                .accessToken();

        return RestClient.builder()
                .baseUrl(baseUrl)
                .defaultHeader("Authorization", "Bearer " + token)
                .build();
    }

    public record TokenRequest(String username, String password) {}

    public record TokenResponse(
            String accessToken,
            String tokenType,
            long expiresIn) {}
}
