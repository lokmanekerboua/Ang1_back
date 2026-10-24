package me.lokmvne.ang1_back.domain.service

import me.lokmvne.ang1_back.config.security.JwtService
import me.lokmvne.ang1_back.domain.mappers.toUserResponse
import me.lokmvne.ang1_back.domain.model.User
import me.lokmvne.ang1_back.domain.port.UserPort
import me.lokmvne.ang1_back.presentation.dto.auth.AuthResponse
import me.lokmvne.ang1_back.presentation.dto.auth.LoginRequest
import me.lokmvne.ang1_back.presentation.dto.user.CreateUserRequest
import me.lokmvne.ang1_back.presentation.dto.user.UserResponse
import org.springframework.http.HttpStatus
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.server.ResponseStatusException

@Service
class AuthService(
    private val userPort: UserPort,
    private val passwordEncoder: PasswordEncoder,
    private val jwtService: JwtService
) {

    @Transactional
    fun register(request: CreateUserRequest): AuthResponse {
        val email = request.email.trim().lowercase()
        val password = passwordEncoder.encode(request.password).let {
            if (it.isNullOrBlank()) {
                throw ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    "Failed to encode password"
                )
            }
            it
        }

        if (userPort.findUserByEmail(email) != null) {
            throw ResponseStatusException(
                HttpStatus.CONFLICT,
                "Email already registered"
            )
        }

        val user = User(
            id = null,
            name = request.name.trim(),
            email = email.lowercase(),
            password = password
        )

        val savedUser = userPort.createUser(user).toUserResponse()

        return generateAuthResponse(savedUser)
    }

    fun login(request: LoginRequest): AuthResponse {
        val email = request.email.trim().lowercase()

        val user = userPort.findUserByEmail(email)
            ?: throw invalidCredentials()

        if (!passwordEncoder.matches(
                request.password,
                user.password
            )
        ) {
            throw invalidCredentials()
        }

        return generateAuthResponse(user.toUserResponse())
    }

    @Transactional(readOnly = true)
    fun getCurrentUser(userId: Long): UserResponse {
        val user = userPort.findUserById(userId) ?: throw ResponseStatusException(
            HttpStatus.NOT_FOUND,
            "User not found"
        )

        return user.toUserResponse()
    }

    private fun generateAuthResponse(
        user: UserResponse
    ): AuthResponse {

        val token = jwtService.generateToken(
            userId = requireNotNull(user.id),
            email = user.email
        )

        return AuthResponse(
            accessToken = token
        )
    }

    private fun invalidCredentials() =
        ResponseStatusException(
            HttpStatus.UNAUTHORIZED,
            "Invalid email or password"
        )
}