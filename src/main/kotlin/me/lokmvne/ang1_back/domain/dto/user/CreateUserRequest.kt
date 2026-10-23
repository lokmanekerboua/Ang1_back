package me.lokmvne.ang1_back.domain.dto.user

data class CreateUserRequest(
    val name: String,
    val email: String,
    val password: String
)
