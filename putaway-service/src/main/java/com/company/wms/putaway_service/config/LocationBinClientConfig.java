package com.company.wms.putaway_service.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
public class LocationBinClientConfig {

    @Bean
    public RestClient locationBinRestClient() {
        return RestClient.builder()
                .baseUrl("http://localhost:8082")
                .build();
    }
}