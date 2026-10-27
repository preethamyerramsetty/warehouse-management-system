package com.company.wms.inbound_service.config;

import org.springframework.context.annotation.Configuration;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;

@Configuration
@OpenAPIDefinition(
    info = @Info(
        title = "Inbound Service API",
        version = "v1",
        description = "Create and retrieve goods receipts."
    )
)
public class OpenApiConfig {
}
