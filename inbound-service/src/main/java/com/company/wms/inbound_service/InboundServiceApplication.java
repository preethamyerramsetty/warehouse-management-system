package com.company.wms.inbound_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class InboundServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(
                InboundServiceApplication.class,
                args);
    }
}