package me.lokmvne.ang1_back.presentation

import me.lokmvne.ang1_back.domain.service.UserService
import org.springframework.stereotype.Controller

@Controller
class UserController(
    private val userService: UserService
) {

}