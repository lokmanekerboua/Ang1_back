package me.lokmvne.ang1_back.presentation

import me.lokmvne.ang1_back.domain.service.TaskService
import org.springframework.stereotype.Controller

@Controller
class TaskController(
    private val taskService: TaskService
) {

}