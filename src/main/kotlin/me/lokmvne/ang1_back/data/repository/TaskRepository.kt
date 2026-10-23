package me.lokmvne.ang1_back.data.repository

import me.lokmvne.ang1_back.data.entity.TaskEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface TaskRepository : JpaRepository<TaskEntity, Long> {

}