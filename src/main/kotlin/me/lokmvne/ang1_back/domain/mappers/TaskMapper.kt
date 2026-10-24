package me.lokmvne.ang1_back.domain.mappers

import me.lokmvne.ang1_back.data.utils.TaskStatus
import me.lokmvne.ang1_back.domain.model.Task
import me.lokmvne.ang1_back.presentation.dto.task.CreateTaskRequest
import me.lokmvne.ang1_back.presentation.dto.task.TaskResponse
import me.lokmvne.ang1_back.presentation.dto.task.UpdateTaskRequest

fun Task.toTaskResponse() = TaskResponse(
    id = requireNotNull(this.id),
    title = this.title,
    description = this.description,
    status = this.status,
    userId = this.userId
)


fun CreateTaskRequest.toTask() = Task(
    id = null,
    title = this.title,
    description = this.description,
    status = this.status,
    userId = 0L
)

fun UpdateTaskRequest.toTask() = Task(
    id = null,
    title = this.title ?: "",
    description = this.description,
    status = this.status ?: TaskStatus.TODO,
    userId = 0L
)