package me.lokmvne.ang1_back.domain.dto.task

import me.lokmvne.ang1_back.data.utils.TaskStatus

data class UpdateTaskRequest(
    val title: String?,
    val description: String?,
    val status: TaskStatus?
)
