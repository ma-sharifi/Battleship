package com.example.battleship.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Mahdi Sharifi
 */
@Configuration
public class OpenApiConfig {
    @Bean
    public OpenAPI openApi() {
        return new OpenAPI()
                .info(new Info()
                        .title("Battleship")
                        .description("Battleship board game ")
                        .version("v1")
                        .contact(new Contact()
                                .name("Mahdi Sharifi")
                                .url("https://www.linkedin.com/in/mahdisharifi/")
                                .email("mahdi.elu@gmail.com"))
                );
    }
}
