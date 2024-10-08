package com.fullsnacke.eimsfuhcmbe.configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI OpenAPI(){
        return new OpenAPI().info(new Info().title("API-service document")
                .version("v1.0.0")
                .description("This is a document for EISM_FUHCM API-service"));
    }
}
