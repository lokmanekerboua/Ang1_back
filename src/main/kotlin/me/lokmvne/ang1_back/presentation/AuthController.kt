package me.lokmvne.ang1_back.presentation

import me.lokmvne.ang1_back.domain.service.AuthService
import org.springframework.stereotype.Controller

@Controller
class AuthController(
    private val authService: AuthService
) {


}