package me.lokmvne.ang1_back.presentation.dto.user

data class UpdateUserRequest(
    val name: String?,
    val email: String?,
    val password: String?
)
