package me.lokmvne.ang1_back.domain.service

import me.lokmvne.ang1_back.data.utils.TaskStatus
import me.lokmvne.ang1_back.domain.mappers.toTask
import me.lokmvne.ang1_back.domain.mappers.toTaskResponse
import me.lokmvne.ang1_back.domain.model.Task
import me.lokmvne.ang1_back.domain.port.TaskPort
import me.lokmvne.ang1_back.presentation.dto.task.TaskResponse
import me.lokmvne.ang1_back.presentation.dto.task.UpdateTaskRequest
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.web.server.ResponseStatusException

@Service
class TaskService(
    private val taskPort: TaskPort
) {

    fun createTask(
        request: Task,
        userId: Long
    ): TaskResponse {
        return taskPort.createTask(request, userId)?.toTaskResponse()
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
        return task.toTaskResponse()
    }

    fun getMyTasks(userId: Long): MutableList<TaskResponse> {
        return taskPort.findTasksByUserId(userId).map(Task::toTaskResponse).toMutableList()
    }

    fun getMyTasksByStatus(
        status: TaskStatus,
        userId: Long
    ): MutableList<TaskResponse> {
        return taskPort.findTasksByUserId(userId)
            .filter { it.status == status }
            .map(Task::toTaskResponse)
            .toMutableList()
    }

    fun updateTask(
        id: Long,
        request: UpdateTaskRequest,
        userId: Long
    ): TaskResponse {
        val task = taskPort.findTaskById(id)
            ?: throw ResponseStatusException(
                HttpStatus.NOT_FOUND,
                "Task not found"
            )
        checkOwnership(task, userId)
        requireNotNull(task.id)
        return taskPort.updateTask(task.id, request.toTask())?.toTaskResponse()
            ?: throw ResponseStatusException(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "Unable to update task"
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
        task: Task,
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