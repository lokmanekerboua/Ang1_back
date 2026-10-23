package me.lokmvne.ang1_back.data.entity

import jakarta.persistence.*

@Entity
@Table(name = "users")
class UserEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,

    @Column(nullable = false)
    var name: String = "",

    @Column(nullable = false, unique = true)
    var email: String = "",

    @Column(nullable = false)
    var password: String = "",

    @OneToMany(
        mappedBy = "userEntity",
        cascade = [CascadeType.ALL],
        orphanRemoval = true
    )
    var taskEntities: MutableList<TaskEntity> = mutableListOf()
)