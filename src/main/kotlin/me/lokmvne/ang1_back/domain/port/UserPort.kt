package me.lokmvne.ang1_back.domain.port

import me.lokmvne.ang1_back.domain.model.User
import me.lokmvne.ang1_back.presentation.dto.user.CreateUserRequest
import me.lokmvne.ang1_back.presentation.dto.user.UpdateUserRequest
import me.lokmvne.ang1_back.presentation.dto.user.UserResponse

interface UserPort {
    fun findUserById(id: Long): User?
    fun findUserByName(name: String): MutableCollection<User>
    fun findUserByEmail(email: String): User?
    fun createUser(user: User): User
    fun updateUser(id: Long, user: User): User?
    fun deleteUserById(id: Long): Boolean
}