package com.iwombat.person.api.v1

import com.iwombat.person.api.v1.dto.PersonDTO
import com.iwombat.person.model.PersonService
import com.iwombat.person.model.entity.Person
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.doNothing
import com.nhaarman.mockitokotlin2.whenever
import org.hamcrest.CoreMatchers.containsString
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.junit.jupiter.params.shadow.com.univocity.parsers.conversions.Conversions.string
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.test.web.servlet.*
import java.util.*
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status



@ExtendWith(SpringExtension::class)
@WebMvcTest(PersonController::class)
class PersonControllerTest {

    @MockBean
    lateinit var personService : PersonService

    @Autowired
    lateinit var mockMvc: MockMvc

    @Test
    @Throws(Exception::class)
    fun shouldHello() {
        mockMvc.perform(get("/v1/hello")).andExpect(status().isOk())

    }


    @Test
    fun getPersonWhenRequested() {
        val person = Person()
        person.firstName = "Ted"
        person.lastName = "Boop"

        val id = person.id
        whenever(personService.getPersonById(any())).thenReturn(person)


        mockMvc.get("/v1/person/${id}") {
            contentType = MediaType.APPLICATION_JSON
            accept = MediaType.APPLICATION_JSON
        }.andExpect {
            status { isOk() }
            jsonPath ("$.firstName") {
                    value("Ted")
            }
            jsonPath ("$.lastName") {
                value("Boop")
            }
        }.andDo {
            print()
        }
    }

    @Test
    fun savePersonWhenRequested() {


        val id = UUID.randomUUID()
        whenever(personService.createPerson(any())).thenReturn(id)

        mockMvc.post("/v1/person?firstName=ted&lastName=bop&email=ted@foo") {
            contentType = MediaType.APPLICATION_JSON
            accept = MediaType.APPLICATION_JSON
        }.andExpect {
            status { isOk() }
            content { string(containsString(id.toString())) }
        }.andDo {
            print()
        }
    }
    @Test
    fun updatePersonWhenRequested() {
        val person = PersonDTO(UUID.randomUUID(), "Ted","Boop","booopmail", 2)
        val personUpdate = PersonDTO(UUID.randomUUID(), "Ted","Boop","booopmail", 3)

        // service will return the entity object
        val personEntity = PersonHelper.personDTOPersistToDomain(person)
        val personUpdateEntity = PersonHelper.personDTOPersistToDomain(personUpdate)
        val id = personEntity.id
        whenever(personService.updatePerson(personEntity)).thenReturn(id)
        whenever(personService.getPersonById(id)).thenReturn(personUpdateEntity)

        val mapper = jacksonObjectMapper()
        val personJson = mapper.writeValueAsString(person)
        val personUpdateJson =  mapper.writeValueAsString(personUpdate)

        mockMvc.put("/v1/person/${person.id}") {
            contentType = MediaType.APPLICATION_JSON
            accept = MediaType.APPLICATION_JSON
            content = personJson
        }.andExpect {
            status { isOk() }
            content { string(personUpdateJson) }
        }.andDo {
            print()
        }
    }

    @Test
    fun deletePersonWhenRequested() {

        val id = UUID.randomUUID()
        doNothing().whenever(personService).deletePerson(id)

        mockMvc.delete("/v1/person/${id}") {
            contentType = MediaType.APPLICATION_JSON
            accept = MediaType.APPLICATION_JSON
        }.andExpect {
            status { isOk() }
            content { string(containsString(id.toString())) }
        }.andDo {
            print()
        }
    }


}