package me.lokmvne.ang1_back.data.repository

import me.lokmvne.ang1_back.data.entity.UserEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface UserRepository : JpaRepository<UserEntity, Long> {
    fun findByEmail(email: String): UserEntity?
    fun findAllByName(name: String): MutableList<UserEntity>
}