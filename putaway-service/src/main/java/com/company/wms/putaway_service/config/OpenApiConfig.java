
package com.company.wms.putaway_service.config;

import org.springframework.context.annotation.Configuration;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;

@Configuration
@OpenAPIDefinition(
    info = @Info(
        title = "Put-Away Service API",
        version = "v1",
        description = "Create, retrieve and complete put-away tasks."
    )
)
public class OpenApiConfig {
}
