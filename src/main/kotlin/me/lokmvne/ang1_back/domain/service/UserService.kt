package me.lokmvne.ang1_back.domain.service

import me.lokmvne.ang1_back.domain.Port.TaskPort
import me.lokmvne.ang1_back.domain.Port.UserPort
import org.springframework.stereotype.Service

@Service
class UserService(
    private val userPort: UserPort,
    private val taskPort: TaskPort
) {

}