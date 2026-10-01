package com.company.wms.putaway_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class PutawayServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(
                PutawayServiceApplication.class,
                args);
    }
}