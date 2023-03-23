package com.iwombat.person.api.v1

import com.iwombat.person.api.v1.dto.PersonDTO
import com.iwombat.person.model.entity.Person

class PersonHelper {
    companion object {
        fun personDomainToDTOPersist(person : Person) : PersonDTO {
            val email : String = person.email ?: ""
            return PersonDTO(person.id, person.firstName, person.lastName, person.email.orEmpty(), person.version )
        }

        fun personDTOPersistToDomain(personDto : PersonDTO) : Person {
            val person = Person()

            // if it's null let the entity set the default
            if (personDto.id != null) {
                person.id = personDto.id
            }
            person.firstName = personDto.firstName
            person.lastName = personDto.lastName
            person.email = personDto.email
            person.version = personDto.version

            return person
        }

        fun personDTOToDomain(personDto : PersonDTO) : Person {
            val person = Person()
            person.firstName = personDto.firstName
            person.lastName = personDto.lastName
            person.email = personDto.email
            return person
        }
    }
}