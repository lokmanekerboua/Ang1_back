package me.lokmvne.ang1_back.domain.Port

import me.lokmvne.ang1_back.domain.dto.task.UpdateTaskRequest
import me.lokmvne.ang1_back.domain.dto.user.CreateUserRequest
import me.lokmvne.ang1_back.domain.dto.user.UpdateUserRequest
import me.lokmvne.ang1_back.domain.dto.user.UserResponse

interface UserPort {
    fun findUserById(id: Long): UserResponse?
    fun findUserByName(name: String): MutableCollection<UserResponse>
    fun findUserByEmail(email: String): UserResponse?
    fun createUser(user: CreateUserRequest): UserResponse?
    fun updateUser(id: Long, user: UpdateUserRequest): UserResponse?
    fun deleteUserById(id: Long): Boolean
}