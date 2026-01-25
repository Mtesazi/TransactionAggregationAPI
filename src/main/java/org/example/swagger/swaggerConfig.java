package org.example.swagger;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class swaggerConfig {


    @Bean
    public OpenAPI transactionApi() {
        return new OpenAPI()
                .info(new Info()
                        .title("Transaction Aggregation API")
                        .description("Aggregates and categorizes financial transactions")
                        .version("1.0"));
    }
}