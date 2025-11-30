package com.gundan.terragold.config;

import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.OpenAPI;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI terragoldOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Terragold Mining Management API")
                        .description("API documentation for Terragold system")
                        .version("1.0"));
    }
}
