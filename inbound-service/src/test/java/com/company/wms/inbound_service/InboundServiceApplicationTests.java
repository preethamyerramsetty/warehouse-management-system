package com.company.wms.inbound_service;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.web.client.RestClient;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.web.client.RestClient;

@SpringBootTest
class InboundServiceApplicationTests {

	@MockitoBean(name = "warehouseRestClient")
	RestClient warehouseRestClient;

	@Test
	void contextLoads() {
	}

}
