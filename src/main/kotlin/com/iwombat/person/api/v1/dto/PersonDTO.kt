package com.iwombat.person.api.v1.dto

import io.swagger.v3.oas.annotations.media.Schema
import java.util.*

// fully persisted PERSON
class PersonDTO  (
    @field:Schema(
        description = "Person id - assigned at creation to new objects",
        required = false
    )
    val id: UUID? = null,

    val firstName : String,
    val lastName : String,
    val email : String,

    @field:Schema(
        description = "Version of the entity - assigned at creation",
        required = false
    )
    val version: Long = 0
)