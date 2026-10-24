package me.lokmvne.ang1_back.domain.model

import me.lokmvne.ang1_back.data.utils.TaskStatus

data class Task(
    val id: Long?,
    val title: String,
    val description: String?,
    val status: TaskStatus,
    val userId: Long
)