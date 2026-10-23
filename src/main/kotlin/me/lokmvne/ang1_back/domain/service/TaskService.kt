package me.lokmvne.ang1_back.domain.service

import me.lokmvne.ang1_back.domain.Port.TaskPort
import me.lokmvne.ang1_back.domain.dto.task.CreateTaskRequest
import me.lokmvne.ang1_back.domain.dto.task.UpdateTaskRequest
import me.lokmvne.ang1_back.domain.dto.task.TaskResponse
import me.lokmvne.ang1_back.data.utils.TaskStatus
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.web.server.ResponseStatusException

@Service
class TaskService(
    private val taskPort: TaskPort
) {

    fun createTask(
        request: CreateTaskRequest,
        userId: Long
    ): TaskResponse {
        return taskPort.createTask(request, userId)
            ?: throw ResponseStatusException(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "Unable to create task"
            )
    }

    fun getTaskById(
        id: Long,
        userId: Long
    ): TaskResponse {
        val task = taskPort.findTaskById(id)
            ?: throw ResponseStatusException(
                HttpStatus.NOT_FOUND,
                "Task not found"
            )

        checkOwnership(task, userId)
        return task
    }

    fun getMyTasks(userId: Long): MutableCollection<TaskResponse> {
        return taskPort.findTasksByUserId(userId)
    }

    fun getMyTasksByStatus(
        status: TaskStatus,
        userId: Long
    ): List<TaskResponse> {
        return taskPort.findTasksByUserId(userId)
            .filter { it.status == status }
    }

    fun updateTask(
        id: Long,
        request: UpdateTaskRequest,
        userId: Long
    ): TaskResponse {
        val task = getTaskById(id, userId)

        return taskPort.updateTask(task.id, request)
            ?: throw ResponseStatusException(
                HttpStatus.NOT_FOUND,
                "Task not found"
            )
    }

    fun deleteTask(
        id: Long,
        userId: Long
    ) {
        getTaskById(id, userId)

        if (!taskPort.deleteTaskById(id)) {
            throw ResponseStatusException(
                HttpStatus.NOT_FOUND,
                "Task not found"
            )
        }
    }

    private fun checkOwnership(
        task: TaskResponse,
        userId: Long
    ) {
        if (task.userId != userId) {
            throw ResponseStatusException(
                HttpStatus.NOT_FOUND,
                "Task not found"
            )
        }
    }
}