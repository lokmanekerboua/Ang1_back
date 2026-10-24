package me.lokmvne.ang1_back.presentation.dto.task

import me.lokmvne.ang1_back.data.utils.TaskStatus

data class TaskResponse(
    val id: Long,
    val title: String,
    val description: String?,
    val status: TaskStatus,
    val userId: Long
)
