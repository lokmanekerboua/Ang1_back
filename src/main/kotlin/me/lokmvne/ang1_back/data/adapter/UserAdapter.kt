package me.lokmvne.ang1_back.data.adapter

import me.lokmvne.ang1_back.data.mappers.toUser
import me.lokmvne.ang1_back.data.mappers.toUserEntity
import me.lokmvne.ang1_back.data.repository.UserRepository
import me.lokmvne.ang1_back.domain.model.User
import me.lokmvne.ang1_back.domain.port.UserPort
import org.springframework.stereotype.Component

@Component
class UserAdapter(
    private val userRepository: UserRepository,
) : UserPort {
    override fun findUserById(id: Long): User? =
        userRepository.findById(id).map { it.toUser() }.orElse(null)


    override fun findUserByName(name: String): MutableCollection<User> {
        return userRepository.findAllByName(name).map { it.toUser() }.toMutableList()
    }

    override fun findUserByEmail(email: String): User? {
        return userRepository.findByEmail(email)?.toUser()
    }

    override fun createUser(user: User): User {
        val entity = user.toUserEntity()
        return userRepository.save(entity).toUser()
    }

    override fun updateUser(
        id: Long,
        user: User
    ): User? {
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
        return userRepository.save(existingUser).toUser()
    }

    override fun deleteUserById(id: Long): Boolean {
        return if (userRepository.existsById(id)) {
            userRepository.deleteById(id)
            true
        } else {
            false
        }
    }
}