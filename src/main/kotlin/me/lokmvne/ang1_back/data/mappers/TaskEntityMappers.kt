package me.lokmvne.ang1_back.data.mappers

import me.lokmvne.ang1_back.data.entity.TaskEntity
import me.lokmvne.ang1_back.data.entity.UserEntity
import me.lokmvne.ang1_back.domain.model.Task

fun TaskEntity.toTask() = Task(
    id = requireNotNull(this.id),
    title = this.title,
    description = this.description,
    status = this.status,
    userId = requireNotNull(this.user?.id)
)

fun Task.toTaskEntity(user: UserEntity) = TaskEntity(
    id = this.id,
    title = this.title,
    description = this.description,
    status = this.status,
    user = user
)