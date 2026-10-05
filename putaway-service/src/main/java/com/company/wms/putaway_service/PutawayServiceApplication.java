package com.company.wms.putaway_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableKafka
@EnableScheduling
@SpringBootApplication
public class PutawayServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(
                PutawayServiceApplication.class,
                args);
    }
}