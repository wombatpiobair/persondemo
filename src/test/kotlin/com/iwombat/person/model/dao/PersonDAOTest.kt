package com.iwombat.person.model.dao

import com.iwombat.person.model.entity.Person
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager
import org.springframework.data.repository.findByIdOrNull
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import org.junit.jupiter.api.Assertions.fail
import org.springframework.orm.ObjectOptimisticLockingFailureException


@DataJpaTest
class PersonDAOTest {
    @Autowired
    lateinit var entityManager: TestEntityManager

    @Autowired
    lateinit var personDao: PersonDAO

    @Test
    fun givenPerson_whenSaved_thenFound() {
        var person : Person = Person()
        person.firstName = "John"

        entityManager.persistAndFlush(person)

        val personFound = personDao.findByIdOrNull(person.id)

        assertTrue(personFound?.firstName == person.firstName)

    }

    @Test
    fun givenPerson_whenSaved_thenFoundByLastName() {
        var person1 : Person = Person()
        person1.firstName = "John"
        person1.lastName = "Smith"
        person1.email = "john.smith@gmail.com"

        entityManager.persistAndFlush(person1)

        var person2 : Person = Person()
        person2.firstName = "Fred"
        person2.lastName = "Smith"
        person1.email = "fred.smith@gmail.com"
        entityManager.persistAndFlush(person2)


        val people = personDao.findPersonByLastName("Smith")

        assertTrue(people.size == 2)

    }

    @Test
    fun givenPerson_whenSaved_thenDeleted() {
        var person : Person = Person()
        person.firstName = "John"

        entityManager.persistAndFlush(person)

        assertDoesNotThrow("delete threw exception") {
            personDao.deleteById(person.id)
        }

        val personFound = personDao.findByIdOrNull(person.id)
        assertTrue(personFound == null)


    }

    @Test
    fun givenPerson_whenSaved_thenUpdated() {
        var person : Person = Person()
        person.firstName = "John"

        entityManager.persistAndFlush(person)

        val personFound = personDao.findByIdOrNull(person.id)
        personFound!!.firstName = "Fred"
        personDao.save(personFound)


        val personFoundTwo = personDao.findByIdOrNull(person.id)
        assertTrue(personFoundTwo!!.firstName == "Fred")
    }

    @Test
    fun updateStaleShouldThrow() {
        var person : Person = Person()
        person.firstName = "John"

        entityManager.persistAndFlush(person)
        // detach object from further updates
        entityManager.detach(person)

        val personFound = personDao.findByIdOrNull(person.id)
        // serialize found


        personFound!!.firstName = "Fred"
        entityManager.persistAndFlush(personFound)
        // verify version with updated attached object
        assertTrue(personFound.version == person.version+1)

        // update detached entity and save - should be behind a version and fail lock
        person.firstName = "Billy"
        try {
            personDao.save(person)
            fail("Didn't throw lock exception")
        } catch (e : ObjectOptimisticLockingFailureException) {
            println("\n\n ---- ${e.message}\n\n")
        }



    }


}
