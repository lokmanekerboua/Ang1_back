package me.lokmvne.ang1_back.presentation.controller

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import jakarta.validation.Valid
import me.lokmvne.ang1_back.presentation.dto.user.CreateUserRequest
import me.lokmvne.ang1_back.presentation.dto.user.UserResponse
import me.lokmvne.ang1_back.domain.service.AuthService
import me.lokmvne.ang1_back.presentation.dto.auth.AuthResponse
import me.lokmvne.ang1_back.presentation.dto.auth.LoginRequest
import org.springframework.http.HttpStatus
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.oauth2.jwt.Jwt
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/auth")
class AuthController(
    private val authService: AuthService
) {


    @Operation(
        summary = "Register a new user",
        description = "Creates a user account and returns a JWT access token."
    )
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "201", description = "User registered"),
            ApiResponse(responseCode = "400", description = "Invalid request"),
            ApiResponse(responseCode = "409", description = "Email already exists")
        ]
    )
    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    fun register(
        @Valid @RequestBody request: CreateUserRequest
    ): AuthResponse {
        return authService.register(request)
    }


    @Operation(
        summary = "Login",
        description = "Authenticates a user and returns a JWT access token."
    )
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Login successful"),
            ApiResponse(responseCode = "401", description = "Invalid credentials")
        ]
    )
    @PostMapping("/login")
    fun login(
        @Valid @RequestBody request: LoginRequest
    ): AuthResponse {
        return authService.login(request)
    }

    @Operation(
        summary = "Get current user",
        description = "Returns the currently authenticated user's information."
    )
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "User retrieved"),
            ApiResponse(responseCode = "401", description = "Unauthorized")
        ]
    )
    @GetMapping("/me")
    fun me(
        @AuthenticationPrincipal jwt: Jwt
    ): UserResponse {
        val userId = requireNotNull(jwt.subject) {
            "JWT subject is missing"
        }.toLong()

        return authService.getCurrentUser(userId)
    }
}