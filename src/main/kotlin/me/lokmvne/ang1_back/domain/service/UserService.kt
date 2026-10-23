package me.lokmvne.ang1_back.domain.service

import me.lokmvne.ang1_back.domain.Port.UserPort
import me.lokmvne.ang1_back.domain.dto.user.UpdateUserRequest
import me.lokmvne.ang1_back.domain.dto.user.UserResponse
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.web.server.ResponseStatusException

@Service
class UserService(
    private val userPort: UserPort
) {

    fun getUserById(id: Long): UserResponse {
        return userPort.findUserById(id)
            ?: throw ResponseStatusException(
                HttpStatus.NOT_FOUND,
                "User not found"
            )
    }

    fun getCurrentUser(userId: Long): UserResponse {
        return getUserById(userId)
    }

    fun updateUser(
        id: Long,
        request: UpdateUserRequest
    ): UserResponse {
        return userPort.updateUser(id, request)
            ?: throw ResponseStatusException(
                HttpStatus.NOT_FOUND,
                "User not found"
            )
    }

    fun deleteUser(id: Long) {
        val deleted = userPort.deleteUserById(id)

        if (!deleted) {
            throw ResponseStatusException(
                HttpStatus.NOT_FOUND,
                "User not found"
            )
        }
    }
}