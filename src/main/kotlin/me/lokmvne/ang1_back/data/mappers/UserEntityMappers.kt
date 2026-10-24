package me.lokmvne.ang1_back.data.mappers

import me.lokmvne.ang1_back.data.entity.UserEntity
import me.lokmvne.ang1_back.domain.model.User

fun UserEntity.toUser()= User(
    id = requireNotNull(this.id),
    name = this.name,
    email = this.email,
    password = this.password
)

fun User.toUserEntity() = UserEntity(
    id = this.id,
    name = this.name,
    email = this.email,
    password = this.password
)