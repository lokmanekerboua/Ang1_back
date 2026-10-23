package me.lokmvne.ang1_back.data.mappers

import me.lokmvne.ang1_back.data.entity.TaskEntity
import me.lokmvne.ang1_back.data.entity.UserEntity
import me.lokmvne.ang1_back.data.utils.TaskStatus
import me.lokmvne.ang1_back.domain.dto.task.CreateTaskRequest
import me.lokmvne.ang1_back.domain.dto.task.TaskResponse
import me.lokmvne.ang1_back.domain.dto.task.UpdateTaskRequest

fun TaskEntity.toTaskResponse() = TaskResponse(
    id = this.id!!,
    title = this.title,
    description = this.description,
    status = this.status,
    userId = this.user?.id!!
)


fun CreateTaskRequest.toTaskEntity() = TaskEntity(
    title = this.title,
    description = this.description,
    status = this.status,
)

fun UpdateTaskRequest.toTaskEntity(userId: UserEntity) = TaskEntity(
    title = this.title ?: "",
    description = this.description ?: "",
    status = this.status ?: TaskStatus.TODO,
)