package com.company.wms.putaway_service.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.support.BasicAuthenticationInterceptor;
import org.springframework.web.client.RestClient;

@Configuration
public class LocationBinClientConfig {

    @Bean
    public RestClient locationBinRestClient(
            @Value("${wms.location-bin.base-url}") String baseUrl,
            @Value("${wms.location-bin.username}") String username,
            @Value("${wms.location-bin.password}") String password) {

        return RestClient.builder()
                .baseUrl(baseUrl)
                .requestInterceptor(
                        new BasicAuthenticationInterceptor(username, password))
                .build();
    }
}