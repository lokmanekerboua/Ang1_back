package me.lokmvne.ang1_back.domain.service

import me.lokmvne.ang1_back.config.security.JwtService
import me.lokmvne.ang1_back.data.entity.UserEntity
import me.lokmvne.ang1_back.data.mappers.toUserResponse
import me.lokmvne.ang1_back.data.repository.UserRepository
import me.lokmvne.ang1_back.domain.dto.auth.*
import me.lokmvne.ang1_back.domain.dto.user.CreateUserRequest
import me.lokmvne.ang1_back.domain.dto.user.UserResponse
import org.springframework.http.HttpStatus
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.server.ResponseStatusException

@Service
class AuthService(
    private val userRepository: UserRepository,
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

        if (userRepository.findByEmail(email) != null) {
            throw ResponseStatusException(
                HttpStatus.CONFLICT,
                "Email already registered"
            )
        }

        val user = UserEntity(
            name = request.name.trim(),
            email = email.lowercase(),
            password = password
        )

        val savedUser = userRepository.save(user)

        return generateAuthResponse(savedUser)
    }

    fun login(request: LoginRequest): AuthResponse {
        val email = request.email.trim().lowercase()

        val user = userRepository.findByEmail(email)
            ?: throw invalidCredentials()

        if (!passwordEncoder.matches(
                request.password,
                user.password
            )
        ) {
            throw invalidCredentials()
        }

        return generateAuthResponse(user)
    }

    @Transactional(readOnly = true)
    fun getCurrentUser(userId: Long): UserResponse {
        val user = userRepository.findById(userId)
            .orElseThrow {
                ResponseStatusException(
                    HttpStatus.UNAUTHORIZED,
                    "User no longer exists"
                )
            }

        return user.toUserResponse()
    }

    private fun generateAuthResponse(
        user: UserEntity
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