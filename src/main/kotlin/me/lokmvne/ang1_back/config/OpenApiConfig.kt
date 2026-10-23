package me.lokmvne.ang1_back.config

import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.info.Info
import io.swagger.v3.oas.models.security.SecurityRequirement
import io.swagger.v3.oas.models.security.SecurityScheme
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class OpenApiConfig {

    @Bean
    fun customOpenAPI(): OpenAPI {
        val securitySchemeName = "bearerAuth"

        return OpenAPI()
            .info(
                Info()
                    .title("Ang1 API")
                    .version("1.0.0")
                    .description(
                        "REST API for task and user management " +
                                "with JWT authentication."
                    )
            )
            .addSecurityItem(
                SecurityRequirement()
                    .addList(securitySchemeName)
            )
            .schemaRequirement(
                securitySchemeName,
                SecurityScheme()
                    .name(securitySchemeName)
                    .type(SecurityScheme.Type.HTTP)
                    .scheme("bearer")
                    .bearerFormat("JWT")
            )
    }
}