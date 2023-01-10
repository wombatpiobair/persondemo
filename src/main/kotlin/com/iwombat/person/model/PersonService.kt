package com.iwombat.person.model

import com.iwombat.person.model.dao.PersonDAO
import com.iwombat.person.model.entity.Person
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import java.util.*
import javax.persistence.NoResultException


@Service
class PersonService {
    @Autowired
    lateinit var personDAO : PersonDAO


    fun getPersonById  (id : UUID) : Person? {
        return personDAO.findByIdOrNull(id)
    }

    fun getAllPerson() : List<Person> {
        return personDAO.findAll().asSequence().toList()
    }

    fun findPersonByEmail(email : String) : Person {
        return personDAO.findPersonByEmail(email)
    }

    fun findPersonByLastName(lastname : String) : List<Person> {
        return personDAO.findPersonByLastName(lastname)
    }

    fun createPerson (person: Person) : UUID {
        val persisted = personDAO.save(person)
        return persisted.id
    }

    fun updatePerson (person : Person) : UUID {
        var id : UUID
        if (personDAO.existsById(person.id)) {
            personDAO.save(person)
            id = person.id
        } else {
            throw NoResultException("No entity for id: ${person.id} exists")
        }

        return id
    }

    fun deletePerson (personId : UUID) {
        if (personDAO.existsById(personId)) {
            personDAO.deleteById(personId)
        } else {
            throw NoResultException("No entity of id: ${personId} exists")
        }
    }
}