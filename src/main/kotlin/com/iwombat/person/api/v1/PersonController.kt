package com.iwombat.person.api.v1

import com.iwombat.person.api.PersonErrorResponse
import com.iwombat.person.api.PersonNotFoundException
import com.iwombat.person.api.v1.dto.PersonDTO
import com.iwombat.person.api.v1.dto.PersonDTOPersist
import com.iwombat.person.model.entity.Person
import com.iwombat.person.model.PersonService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*
import java.util.*
import javax.persistence.NoResultException


@RestController
@RequestMapping("/v1")
class PersonController {
    @Autowired
    lateinit var personService : PersonService

    @GetMapping("/hello")
    fun hello() : String {
        return "Hello"
    }

    @Operation(summary = "Get a person by ID")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Successful Operation"),
            ApiResponse(responseCode = "404", description = "Person by id does not exist", content = [ Content(
                schema = Schema(
                    implementation = PersonErrorResponse::class
                ) ) ]
            )
        ]
    )
    @GetMapping("/person/{id}")
    fun getPersonById(@PathVariable("id") id: UUID) : PersonDTOPersist {
        val person = personService.getPersonById(id) ?: throw PersonNotFoundException("Person: ${id} Not Found")
        return PersonHelper.personDomainToDTOPersist(person)
    }

    @Operation(summary = "Get all people")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Successful Operation"),
            ApiResponse(responseCode = "500", description = "Unable to fetch records")
        ]
    )
    @GetMapping("/person")
    fun getProjects() : List<PersonDTOPersist> {
        val persons = personService.getAllPerson()
        return persons.map{ PersonHelper.personDomainToDTOPersist(it) }
    }

    @Operation(summary = "Get all people by last name")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Successful Operation"),
            ApiResponse(responseCode = "500", description = "Unable to fetch records")
        ]
    )
    @GetMapping("/persons/{lastname}")
    fun getPersonById(@PathVariable("lastnam e") name: String) : List<PersonDTOPersist> {
        val persons = personService.findPersonByLastName(name) ?: throw PersonNotFoundException("${name} Not Found")
        return persons.map{PersonHelper.personDomainToDTOPersist(it)}
    }

    @Operation(summary = "Create a new person object")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Successful Operation"),
            ApiResponse(responseCode = "500", description = "Unable to save entity")
        ]
    )
    @PostMapping("/person")
    fun createPerson(@RequestBody personDTO : PersonDTO) : UUID {
        val person : Person = PersonHelper.personDTOToDomain(personDTO)
        var id = personService.createPerson(person)

        return id;
    }

    @Operation(summary = "Update a person by ID")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Successful Operation"),
            ApiResponse(responseCode = "404", description = "Person by id does not exist", content = [ Content(
                schema = Schema(
                    implementation = PersonErrorResponse::class
                ) ) ]
            ),
            ApiResponse(responseCode = "409", description = "Stale object", content = [ Content(
                schema = Schema(
                    implementation = PersonErrorResponse::class
                ) ) ]
    )
        ]
    )
    @PutMapping("/person/{id}")
    fun updatePerson(@PathVariable("id") id:UUID, @RequestBody personDTO : PersonDTOPersist) : PersonDTOPersist {
        val person : Person = PersonHelper.personDTOPersistToDomain(personDTO)

        if (person.id != id) throw IllegalArgumentException("Id does not match body")

        try {
            personService.updatePerson(person)
        } catch (e : NoResultException) {
            throw PersonNotFoundException("Person: ${id} Not Found")
        }

        return PersonHelper.personDomainToDTOPersist(personService.getPersonById(id)!!);
    }

    @Operation(summary = "Delete a person by ID")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Successful Operation"),
            ApiResponse(responseCode = "404", description = "Person by id does not exist", content = [ Content(
                schema = Schema(
                    implementation = PersonErrorResponse::class
                ) ) ]
            )
        ]
    )
    @DeleteMapping("/person/{id}")
    fun deletePerson(@PathVariable id : UUID) : UUID {
        try {
            personService.deletePerson(id)
        } catch (e : NoResultException) {
            throw PersonNotFoundException("Person: ${id} Not Found")
        }

        return id
    }

}