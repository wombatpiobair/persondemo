package com.iwombat.person.model

import com.iwombat.person.model.entity.Person
import org.apache.commons.lang3.math.NumberUtils.toLong
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class PersonServiceTest {
    @Autowired
    lateinit var personService : PersonService

    @Test
    fun savedNew_thenFound() {
        var person : Person = Person()
        person.firstName = "John"
        person.lastName = "Smith"
        person.email = "john.smith1@google.com"

        val id = person.id

        val newId = personService.createPerson(person)

        val foundPerson = personService.getPersonById(newId)!!

        assertTrue(person.id == id)
        assertTrue(person.version == 1L)
        assertTrue(person.firstName == foundPerson.firstName)
        assertTrue(person.lastName == foundPerson.lastName)
        assertTrue(person.email == foundPerson.email)
    }

    @Test
    fun savedNew_thenUpdate() {
        var person : Person = Person()
        person.firstName = "John"
        person.lastName = "Smith"
        person.email = "john.smith1@google.com"
        val newId = personService.createPerson(person)

        val personUpdate = personService.getPersonById(newId)!!
        personUpdate.email = "johnnys@hotmail.com"
        personService.updatePerson(personUpdate)

        val foundPerson = personService.getPersonById(newId)

        assertTrue(foundPerson!!.email == "johnnys@hotmail.com")
    }

    @Test
    fun savedNew_thenDelete() {
        var person : Person = Person()
        person.firstName = "John"
        person.lastName = "Smith"
        person.email = "john.smith1@google.com"
        val newId = personService.createPerson(person)

        val personToDeleteId = personService.getPersonById(newId)!!.id
        personService.deletePerson(personToDeleteId)

        assertTrue(personService.getPersonById(personToDeleteId) == null)
    }


}