package com.iwombat.person.model.dao

import com.iwombat.person.model.entity.Person
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface PersonDAO : CrudRepository<Person, UUID> {
    fun findPersonByEmail(email : String) : Person
    fun findPersonByEmailAndFirstName(email: String, firstname : String) : Person
    fun findPersonByLastName(lastname: String) : List<Person>
}
