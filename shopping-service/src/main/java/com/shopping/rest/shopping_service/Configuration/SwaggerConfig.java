package com.shopping.rest.shopping_service.Configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI springShopOpenAPI() {
        return new OpenAPI()
                .info(new Info().title("shopping-service API")
                .description("Shopping service application")
                .version("v0.0.1")
                .license(new License().name("Apache 2.0").url("http://springdoc.org")));
                
    }
}