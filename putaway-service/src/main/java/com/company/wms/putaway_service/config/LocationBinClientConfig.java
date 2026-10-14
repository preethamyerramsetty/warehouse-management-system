
package com.company.wms.putaway_service.config;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.web.client.RestClient;

@Configuration
public class LocationBinClientConfig {

    @Bean
    public RestClient locationBinRestClient() {
        String credentials = "wmsuser:ChangeMe123!";
        String encodedCredentials = Base64.getEncoder()
                .encodeToString(credentials.getBytes(StandardCharsets.UTF_8));

        return RestClient.builder()
                .baseUrl("http://localhost:8082")
                .defaultHeader(
                        HttpHeaders.AUTHORIZATION,
                        "Basic " + encodedCredentials)
                .build();
    }
}
