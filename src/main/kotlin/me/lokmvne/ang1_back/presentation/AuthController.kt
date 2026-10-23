package me.lokmvne.ang1_back.presentation

import jakarta.validation.Valid
import me.lokmvne.ang1_back.domain.dto.auth.*
import me.lokmvne.ang1_back.domain.dto.user.CreateUserRequest
import me.lokmvne.ang1_back.domain.dto.user.UserResponse
import me.lokmvne.ang1_back.domain.service.AuthService
import org.springframework.http.HttpStatus
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.oauth2.jwt.Jwt
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/auth")
class AuthController(
    private val authService: AuthService
) {

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    fun register(
        @Valid @RequestBody request: CreateUserRequest
    ): AuthResponse {
        return authService.register(request)
    }

    @PostMapping("/login")
    fun login(
        @Valid @RequestBody request: LoginRequest
    ): AuthResponse {
        return authService.login(request)
    }

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