package me.lokmvne.ang1_back.data.adapter

import me.lokmvne.ang1_back.data.mappers.toTask
import me.lokmvne.ang1_back.data.mappers.toTaskEntity
import me.lokmvne.ang1_back.data.repository.TaskRepository
import me.lokmvne.ang1_back.data.repository.UserRepository
import me.lokmvne.ang1_back.data.utils.TaskStatus
import me.lokmvne.ang1_back.domain.model.Task
import me.lokmvne.ang1_back.domain.port.TaskPort
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Component
import org.springframework.web.server.ResponseStatusException

@Component
class TaskAdapter(
    private val taskRepository: TaskRepository,
    private val userRepository: UserRepository
) : TaskPort {
    override fun createTask(
        task: Task,
        userId: Long
    ): Task {

        val user = userRepository.findById(userId)
            .orElseThrow {
                ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "User not found"
                )
            }

        val entity = task.toTaskEntity(user)

        return taskRepository.save(entity).toTask()
    }

    override fun updateTask(
        id: Long,
        task: Task
    ): Task? {
        val existingTask = taskRepository.findById(id).orElseThrow {
            ResponseStatusException(
                HttpStatus.NOT_FOUND,
                "Task not found with id: $id"
            )
        }
        existingTask.apply {
            title = task.title ?: title
            description = task.description ?: description
            status = task.status ?: status
        }
        return taskRepository.save(existingTask).toTask()
    }

    override fun findTaskById(id: Long): Task? {
        return taskRepository.findById(id).map { it.toTask() }.orElse(null)
    }

    override fun findAllTasks(): MutableCollection<Task> {
        return taskRepository.findAll().map { it.toTask() }.toMutableList()
    }

    override fun findTasksByUserId(userId: Long): MutableCollection<Task> {
        val user = userRepository.existsById(userId).also {
            if (!it) throw ResponseStatusException(HttpStatus.NOT_FOUND, "User not found with id: $userId")
        }
        return taskRepository.findByUserId(userId = userId).map { it.toTask() }.toMutableList()
    }

    override fun findTasksByStatus(status: TaskStatus): MutableCollection<Task> {
        return taskRepository.findByStatus(status).map { it.toTask() }.toMutableList()
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