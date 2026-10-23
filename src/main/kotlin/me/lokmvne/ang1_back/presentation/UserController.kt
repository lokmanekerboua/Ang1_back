package me.lokmvne.ang1_back.presentation

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import me.lokmvne.ang1_back.domain.dto.user.UpdateUserRequest
import me.lokmvne.ang1_back.domain.dto.user.UserResponse
import me.lokmvne.ang1_back.domain.service.UserService
import org.springframework.http.HttpStatus
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.oauth2.jwt.Jwt
import org.springframework.web.bind.annotation.*
import org.springframework.web.server.ResponseStatusException

@RestController
@RequestMapping("/api/users")
@Tag(
    name = "Users",
    description = "User profile management"
)
class UserController(
    private val userService: UserService
) {

    @Operation(summary = "Get current authenticated user")
    @GetMapping("/me")
    fun getMe(
        @Parameter(hidden = true)
        @AuthenticationPrincipal jwt: Jwt
    ): UserResponse {
        return userService.getCurrentUser(jwt.userId())
    }

    @Operation(summary = "Get user by ID")
    @GetMapping("/{id}")
    fun getUserById(
        @PathVariable id: Long,
        @Parameter(hidden = true)
        @AuthenticationPrincipal jwt: Jwt
    ): UserResponse {
        checkOwnership(id, jwt)

        return userService.getUserById(id)
    }

    @Operation(summary = "Update user profile")
    @PutMapping("/{id}")
    fun updateUser(
        @PathVariable id: Long,
        @Valid @RequestBody request: UpdateUserRequest,
        @Parameter(hidden = true)
        @AuthenticationPrincipal jwt: Jwt
    ): UserResponse {
        checkOwnership(id, jwt)

        return userService.updateUser(id, request)
    }

    @Operation(summary = "Delete user account")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun deleteUser(
        @PathVariable id: Long,
        @Parameter(hidden = true)
        @AuthenticationPrincipal jwt: Jwt
    ) {
        checkOwnership(id, jwt)

        userService.deleteUser(id)
    }

    private fun checkOwnership(id: Long, jwt: Jwt) {
        if (jwt.userId() != id) {
            throw ResponseStatusException(
                HttpStatus.FORBIDDEN,
                "Access denied"
            )
        }
    }

    private fun Jwt.userId(): Long {
        return subject?.toLongOrNull()
            ?: throw ResponseStatusException(
                HttpStatus.UNAUTHORIZED,
                "Invalid JWT subject"
            )
    }
}