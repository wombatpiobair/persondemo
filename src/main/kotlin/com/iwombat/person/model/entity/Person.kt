package com.iwombat.person.model.entity


import java.util.*
import javax.persistence.*

@Entity
class Person {
    @Column(nullable = false)
    var firstName: String = ""

    @Column(nullable = false)
    var lastName: String = ""

    @Column(nullable = true)
    var email: String? = null
    @Id
    @Column(name = "id", length = 16, unique = true, nullable = false)
    var id: UUID =  UUID.randomUUID()

    @Version
    var version: Long = 1

    fun forceUnique() {
        id = UUID.randomUUID()
    }
}



