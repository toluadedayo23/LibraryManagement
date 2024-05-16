package com.test.librarymanagement.config.openapi;


import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.util.Collections;

@Configuration
public class OpenApiConfig {
    private static final String API_KEY = "AuthenticationToken";
    @Value("${librarymanagement.swagger.title}")
    private String title;
    @Value("${librarymanagement.swagger.description}")
    private String description;

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .components(new Components()
                        .addSecuritySchemes(API_KEY, globalAuthorizationTokenKey()))
                .info(new Info()
                        .title(title)
                        .description(description))
                .security(Collections.singletonList(new SecurityRequirement().addList(API_KEY)));
    }

    private SecurityScheme globalAuthorizationTokenKey() {
        return new SecurityScheme()
                .name("Authorization")
                .description("Add Bearer token -- Remember to include \"Bearer\" prefix")
                .in(SecurityScheme.In.HEADER)
                .type(SecurityScheme.Type.APIKEY);
    }

}

