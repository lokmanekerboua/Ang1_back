package me.lokmvne.ang1_back.config.security

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.security.config.Customizer
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.oauth2.jwt.JwtDecoder
import org.springframework.security.oauth2.jwt.JwtEncoder
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter
import org.springframework.security.web.SecurityFilterChain
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.CorsConfigurationSource
import org.springframework.web.cors.UrlBasedCorsConfigurationSource
import org.springframework.beans.factory.annotation.Value
import com.nimbusds.jose.jwk.source.ImmutableSecret
import javax.crypto.spec.SecretKeySpec

@Configuration
class SecurityConfig(
    @Value("\${app.jwt.secret}")
    private val jwtSecret: String
) {

    init {
        require(jwtSecret.toByteArray(Charsets.UTF_8).size >= 32) {
            "JWT secret must contain at least 32 bytes"
        }
    }


    @Bean
    fun passwordEncoder(): PasswordEncoder =
        BCryptPasswordEncoder()

    @Bean
    fun jwtEncoder(): JwtEncoder {
        val key = SecretKeySpec(
            jwtSecret.toByteArray(Charsets.UTF_8),
            "HmacSHA256"
        )
        return NimbusJwtEncoder(ImmutableSecret(key))
    }

    @Bean
    fun jwtDecoder(): JwtDecoder {
        val key = SecretKeySpec(
            jwtSecret.toByteArray(Charsets.UTF_8),
            "HmacSHA256"
        )

        return NimbusJwtDecoder
            .withSecretKey(key)
            .build()
    }

    @Bean
    fun securityFilterChain(
        http: HttpSecurity
    ): SecurityFilterChain {

        http
            .csrf { it.disable() }
            .cors(Customizer.withDefaults())
            .sessionManagement {
                it.sessionCreationPolicy(
                    SessionCreationPolicy.STATELESS
                )
            }
            .authorizeHttpRequests {
                // Public endpoints
                it.requestMatchers(
                    HttpMethod.POST,
                    "/api/auth/register",
                    "/api/auth/login"
                ).permitAll()

                // Swagger
                it.requestMatchers(
                    "/swagger-ui/**",
                    "/swagger-ui.html",
                    "/v3/api-docs/**",
                    "/v3/api-docs"
                ).permitAll()

                // CORS preflight
                it.requestMatchers(
                    HttpMethod.OPTIONS,
                    "/**"
                ).permitAll()

                // All other endpoints require JWT
                it.anyRequest().authenticated()
            }
            .oauth2ResourceServer {
                it.jwt(Customizer.withDefaults())
            }

        return http.build()
    }

    @Bean
    fun corsConfigurationSource(): CorsConfigurationSource {
        val config = CorsConfiguration().apply {
            allowedOrigins = listOf("http://localhost:4200")
            allowedMethods = listOf(
                "GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"
            )
            allowedHeaders = listOf("*")
            allowCredentials = true
        }

        return UrlBasedCorsConfigurationSource().apply {
            registerCorsConfiguration("/**", config)
        }
    }
}