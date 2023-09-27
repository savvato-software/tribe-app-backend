package com.savvato.tribeapp.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import java.util.List;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;

public class OpenApiConfig {
  @Bean
  public OpenAPI openApi(@Value("${DEV_SERVER}") String devServer) {
    return new OpenAPI()
        .components(new Components())
        .info(
            new Info()
                .title("TribeApp API")
                .version("1.0.0")
                .license(
                    new License()
                        .name("Apache 2.0")
                        .url("https://www.apache.org/licenses/LICENSE-2.0"))
                .contact(new Contact().email("me")))
        .servers(
            List.of(
                new io.swagger.v3.oas.models.servers.Server()
                    .description("Development server")
                    .url(devServer)))
        .security(
            List.of(
                new io.swagger.v3.oas.models.security.SecurityRequirement()
                    .addList("Bearer Authentication")));
  }
}
