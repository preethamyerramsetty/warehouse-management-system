
package com.company.wms.location_bin_service.config;

import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;

@Configuration
@OpenAPIDefinition(
    info = @Info(
        title = "Location and Bin Service API",
        version = "v1",
        description = "Create and retrieve warehouse locations and bins."
    )
)
public class OpenApiConfig {
}
