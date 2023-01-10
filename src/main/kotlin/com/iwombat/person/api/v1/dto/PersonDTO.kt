package com.iwombat.person.api.v1.dto

import io.swagger.v3.oas.annotations.media.Schema
import java.util.*

// PersonDTO for creating a new person - could just be url attributes in a REST request instead of a body
@Schema(description = "Model for a person entity.")
open class PersonDTO (

    val firstName: String,
    val lastName: String,
    @field:Schema(
        description = "valid e-mail",
        required = false
    )
    val email: String

    )

// fully persisted PERSON
class PersonDTOPersist  (
    @field:Schema(
        description = "Person id - assigned at creation to new objects",
        required = false
    )
    val id: UUID? = null,

    firstName : String,
    lastName : String,
    email : String,

    @field:Schema(
        description = "Version of the entity - assigned at creation",
        required = false
    )
    val version: Long = 0
) : PersonDTO(firstName , lastName, email)