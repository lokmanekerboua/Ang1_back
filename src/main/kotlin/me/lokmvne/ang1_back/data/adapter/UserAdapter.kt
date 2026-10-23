package me.lokmvne.ang1_back.data.adapter

import me.lokmvne.ang1_back.data.mappers.toUserEntity
import me.lokmvne.ang1_back.data.mappers.toUserResponse
import me.lokmvne.ang1_back.data.repository.UserRepository
import me.lokmvne.ang1_back.domain.Port.UserPort
import me.lokmvne.ang1_back.domain.dto.user.CreateUserRequest
import me.lokmvne.ang1_back.domain.dto.user.UpdateUserRequest
import me.lokmvne.ang1_back.domain.dto.user.UserResponse
import org.springframework.stereotype.Component

@Component
class UserAdapter(
    private val userRepository: UserRepository
) : UserPort {
    override fun findUserById(id: Long): UserResponse? =
        userRepository.findById(id).map { it.toUserResponse() }.orElse(null)


    override fun findUserByName(name: String): MutableCollection<UserResponse> {
        return userRepository.findAllByName(name).map { it.toUserResponse() }.toMutableList()
    }

    override fun findUserByEmail(email: String): UserResponse? {
        return userRepository.findByEmail(email)?.toUserResponse()
    }

    override fun createUser(user: CreateUserRequest): UserResponse? {
        return userRepository.save(user.toUserEntity()).toUserResponse()
    }

    override fun updateUser(
        id: Long,
        user: UpdateUserRequest
    ): UserResponse? {
        val existingUser = userRepository.findById(id).orElseThrow {
            throw Exception("User not found with id: $id")
        }
        existingUser.apply {
            this.apply {
                name = user.name ?: name
                email = user.email ?: email
                password = user.password ?: password
            }
        }
        return userRepository.save(existingUser).toUserResponse()
    }

    override fun deleteUserById(id: Long): Boolean {
        TODO("Not yet implemented")
    }

}