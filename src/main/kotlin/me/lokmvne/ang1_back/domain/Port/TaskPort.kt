package me.lokmvne.ang1_back.domain.Port

import me.lokmvne.ang1_back.data.utils.TaskStatus
import me.lokmvne.ang1_back.domain.dto.task.CreateTaskRequest
import me.lokmvne.ang1_back.domain.dto.task.TaskResponse
import me.lokmvne.ang1_back.domain.dto.task.UpdateTaskRequest

interface TaskPort {
    fun createTask(task: CreateTaskRequest): TaskResponse?
    fun updateTask(id: Long, task: UpdateTaskRequest): TaskResponse?
    fun findTaskById(id: Long): TaskResponse?
    fun findAllTasks(): MutableCollection<TaskResponse>
    fun findTasksByUserId(userId: Long): MutableCollection<TaskResponse>
    fun findTasksByStatus(status: TaskStatus): MutableCollection<TaskResponse>
    fun deleteTaskById(id: Long): Boolean
}