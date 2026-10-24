package me.lokmvne.ang1_back.presentation.controller

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import me.lokmvne.ang1_back.data.utils.TaskStatus
import me.lokmvne.ang1_back.domain.mappers.toTask
import me.lokmvne.ang1_back.presentation.dto.task.CreateTaskRequest
import me.lokmvne.ang1_back.presentation.dto.task.UpdateTaskRequest
import me.lokmvne.ang1_back.presentation.dto.task.TaskResponse
import me.lokmvne.ang1_back.domain.service.TaskService
import org.springframework.http.HttpStatus
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.oauth2.jwt.Jwt
import org.springframework.web.bind.annotation.*
import org.springframework.web.server.ResponseStatusException

@RestController
@RequestMapping("/api/tasks")
@Tag(
    name = "Tasks",
    description = "Authenticated user's task management"
)
class TaskController(
    private val taskService: TaskService
) {

    @Operation(summary = "Create a new task")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun createTask(
        @Valid @RequestBody request: CreateTaskRequest,
        @Parameter(hidden = true)
        @AuthenticationPrincipal jwt: Jwt
    ): TaskResponse {
        return taskService.createTask(request.toTask(), jwt.userId())
    }

    @Operation(summary = "Get all my tasks")
    @GetMapping
    fun getMyTasks(
        @Parameter(hidden = true)
        @AuthenticationPrincipal jwt: Jwt
    ): Collection<TaskResponse> {
        return taskService.getMyTasks(jwt.userId())
    }

    @Operation(summary = "Get my tasks by status")
    @GetMapping("/status/{status}")
    fun getMyTasksByStatus(
        @PathVariable status: TaskStatus,
        @Parameter(hidden = true)
        @AuthenticationPrincipal jwt: Jwt
    ): MutableList<TaskResponse> {
        return taskService.getMyTasksByStatus(
            status,
            jwt.userId()
        )
    }

    @Operation(summary = "Get a task by ID")
    @GetMapping("/{id}")
    fun getTaskById(
        @PathVariable id: Long,
        @Parameter(hidden = true)
        @AuthenticationPrincipal jwt: Jwt
    ): TaskResponse {
        return taskService.getTaskById(id, jwt.userId())
    }

    @Operation(summary = "Update a task")
    @PutMapping("/{id}")
    fun updateTask(
        @PathVariable id: Long,
        @Valid @RequestBody request: UpdateTaskRequest,
        @Parameter(hidden = true)
        @AuthenticationPrincipal jwt: Jwt
    ): TaskResponse {
        return taskService.updateTask(
            id,
            request,
            jwt.userId()
        )
    }

    @Operation(summary = "Delete a task")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun deleteTask(
        @PathVariable id: Long,
        @Parameter(hidden = true)
        @AuthenticationPrincipal jwt: Jwt
    ) {
        taskService.deleteTask(id, jwt.userId())
    }

    private fun Jwt.userId(): Long {
        return subject?.toLongOrNull()
            ?: throw ResponseStatusException(
                HttpStatus.UNAUTHORIZED,
                "Invalid JWT subject"
            )
    }
}