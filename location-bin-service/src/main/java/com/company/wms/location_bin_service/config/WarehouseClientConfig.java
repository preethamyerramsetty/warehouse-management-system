package com.company.wms.location_bin_service.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.support.BasicAuthenticationInterceptor;
import org.springframework.web.client.RestClient;

@Configuration
public class WarehouseClientConfig {

    @Bean
    public RestClient warehouseRestClient(
            @Value("${wms.warehouse.base-url}") String baseUrl,
            @Value("${wms.warehouse.username}") String username,
            @Value("${wms.warehouse.password}") String password) {

        return RestClient.builder()
                .baseUrl(baseUrl)
                .requestInterceptor(
                        new BasicAuthenticationInterceptor(username, password))
                .build();
    }
}