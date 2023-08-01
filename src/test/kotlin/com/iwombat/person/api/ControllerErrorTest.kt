package com.iwombat.person.api

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.server.LocalServerPort
import org.springframework.http.*
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.web.client.HttpStatusCodeException
import org.springframework.web.client.RestTemplate
import java.util.*


@ExtendWith(SpringExtension::class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ControllerErrorTest {

    @LocalServerPort
    var port: Int = 0

    @Test
    fun servletShouldGiveGeneric404ErrorForBadURL() {
        val id = UUID.randomUUID()
        val restTemplate = RestTemplate()

        val url = "http://localhost:${port}/v2/person/${id}"
        try {
            restTemplate.getForEntity(url, String::class.java)
            fail("Did not throw exception")
        } catch ( e : HttpStatusCodeException) {
            val status = e.statusCode
            val body = e.responseBodyAsString
            assertEquals(status, HttpStatus.NOT_FOUND)
            assertTrue(body.contains("Endpoint Not Found"))
            print(body)
        }

    }

    @Test
    fun controllerShouldGive404ErrorForUnknownID() {
        val id = UUID.randomUUID()
        val restTemplate = RestTemplate()

        val url = "http://localhost:${port}/v1/person/${id}"
        try {
            restTemplate.getForEntity(url, String::class.java)
            fail("Did not throw exception")
        } catch ( e : HttpStatusCodeException) {
            val status = e.statusCode
            val body = e.responseBodyAsString
            assertEquals(HttpStatus.NOT_FOUND, status)
            assertTrue(body.contains("Person:"))
            assertTrue(body.contains("Not Found"))
            print(body)
        }

    }

    @Test
    fun jacksonShouldGiveParseErrorForBadObject() {
        val person = BadPersonDTO(UUID.randomUUID(), "Ted")
        val restTemplate = RestTemplate()

        val url = "http://localhost:${port}/v1/person/${person.id}"
        val headers = HttpHeaders()
        headers.contentType = MediaType.APPLICATION_JSON

        val requestEntity = HttpEntity<BadPersonDTO>(person, headers)
        try {
            restTemplate.exchange(url, HttpMethod.PUT, requestEntity, String::class.java)
            fail("Did not throw exception")
        } catch ( e : HttpStatusCodeException) {
            val status = e.statusCode
            val body = e.responseBodyAsString
            assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, status)
            assertTrue(body.contains("Unreadable Message"))
            print(body)
        }

    }
}

class BadPersonDTO  (
    val id: UUID? = null,
    val name : String
)
