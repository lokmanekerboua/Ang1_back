package me.lokmvne.ang1_back.data.mappers

import me.lokmvne.ang1_back.data.entity.UserEntity
import me.lokmvne.ang1_back.domain.dto.user.CreateUserRequest
import me.lokmvne.ang1_back.domain.dto.user.UpdateUserRequest
import me.lokmvne.ang1_back.domain.dto.user.UserResponse

fun UserEntity.toUserResponse() = UserResponse(
    id = requireNotNull(this.id),
    email = this.email,
    name = this.email,
)

fun CreateUserRequest.toUserEntity() = UserEntity(
    name = this.name,
    email = this.email,
    password = this.password
)

fun UpdateUserRequest.toUserEntity() = UserEntity(
    name = this.name ?: "",
    email = this.email ?: "",
    password = this.password ?: ""
)