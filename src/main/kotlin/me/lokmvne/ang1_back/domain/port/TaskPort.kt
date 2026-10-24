package me.lokmvne.ang1_back.domain.port

import me.lokmvne.ang1_back.data.utils.TaskStatus
import me.lokmvne.ang1_back.domain.model.Task

interface TaskPort {
    fun createTask(task: Task, userId: Long): Task?
    fun updateTask(id: Long, task: Task): Task?
    fun findTaskById(id: Long): Task?
    fun findAllTasks(): MutableCollection<Task>
    fun findTasksByUserId(userId: Long): MutableCollection<Task>
    fun findTasksByStatus(status: TaskStatus): MutableCollection<Task>
    fun deleteTaskById(id: Long): Boolean
}