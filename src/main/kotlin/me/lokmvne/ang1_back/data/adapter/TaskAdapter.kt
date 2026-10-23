package me.lokmvne.ang1_back.data.adapter

import me.lokmvne.ang1_back.data.mappers.toTaskEntity
import me.lokmvne.ang1_back.data.mappers.toTaskResponse
import me.lokmvne.ang1_back.data.repository.TaskRepository
import me.lokmvne.ang1_back.data.repository.UserRepository
import me.lokmvne.ang1_back.data.utils.TaskStatus
import me.lokmvne.ang1_back.domain.Port.TaskPort
import me.lokmvne.ang1_back.domain.dto.task.CreateTaskRequest
import me.lokmvne.ang1_back.domain.dto.task.TaskResponse
import me.lokmvne.ang1_back.domain.dto.task.UpdateTaskRequest
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Component
import org.springframework.web.server.ResponseStatusException

@Component
class TaskAdapter(
    private val taskRepository: TaskRepository,
    private val userRepository: UserRepository
) : TaskPort {
    override fun createTask(
        task: CreateTaskRequest,
        userId: Long
    ): TaskResponse {

        val user = userRepository.findById(userId)
            .orElseThrow {
                ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "User not found"
                )
            }

        val entity = task.toTaskEntity().apply {
            this.user = user
        }

        return taskRepository.save(entity).toTaskResponse()
    }

    override fun updateTask(
        id: Long,
        task: UpdateTaskRequest
    ): TaskResponse? {
        val existingTask = taskRepository.findById(id).orElseThrow {
            ResponseStatusException(
                HttpStatus.NOT_FOUND,
                "Task not found with id: $id"
            )
        }
        existingTask.apply {
            title = task.title ?: title
            description = task.description ?: description
            status = task.status?: status
        }
        return taskRepository.save(existingTask).toTaskResponse()
    }

    override fun findTaskById(id: Long): TaskResponse? {
        return taskRepository.findById(id).map { it.toTaskResponse() }.orElse(null)
    }

    override fun findAllTasks(): MutableCollection<TaskResponse> {
        return taskRepository.findAll().map { it.toTaskResponse() }.toMutableList()
    }

    override fun findTasksByUserId(userId: Long): MutableCollection<TaskResponse> {
        val user = userRepository.existsById(userId).also {
            if (!it) throw ResponseStatusException(HttpStatus.NOT_FOUND, "User not found with id: $userId")
        }
        return taskRepository.findByUserId(userId = userId).map { it.toTaskResponse() }.toMutableList()
    }

    override fun findTasksByStatus(status: TaskStatus): MutableCollection<TaskResponse> {
        return taskRepository.findByStatus(status).map { it.toTaskResponse() }.toMutableList()
    }

    override fun deleteTaskById(id: Long): Boolean {
        val existingTask = taskRepository.findById(id).orElseThrow {
            ResponseStatusException(
                HttpStatus.NOT_FOUND,
                "Task not found with id: $id"
            )
        }
        taskRepository.delete(existingTask)
        return true
    }
}