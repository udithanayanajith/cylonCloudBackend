package com.uditha.loan_service.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {
    @Bean
    public OpenAPI orderServiceOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Loan service")
                        .description("API for processing managing loan data.")
                        .version("1.0")
                        .contact(new Contact()
                                .name("Uditha")
                                .email("uditha.com")));
    }
}
