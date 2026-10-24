package me.lokmvne.ang1_back.domain.mappers

import me.lokmvne.ang1_back.data.entity.UserEntity
import me.lokmvne.ang1_back.domain.model.User
import me.lokmvne.ang1_back.presentation.dto.user.CreateUserRequest
import me.lokmvne.ang1_back.presentation.dto.user.UpdateUserRequest
import me.lokmvne.ang1_back.presentation.dto.user.UserResponse

fun User.toUserResponse() = UserResponse(
    id = requireNotNull(this.id),
    email = this.email,
    name = this.email,
)

fun CreateUserRequest.toUser() = User(
    id = null,
    name = this.name,
    email = this.email,
    password = this.password
)

fun UpdateUserRequest.toUser() = User(
    id = null,
    name = this.name ?: "",
    email = this.email ?: "",
    password = this.password ?: ""
)