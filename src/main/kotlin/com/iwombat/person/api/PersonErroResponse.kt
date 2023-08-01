package com.iwombat.person.api

import org.hibernate.StaleObjectStateException
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.http.converter.HttpMessageNotReadableException
import org.springframework.web.bind.annotation.*

@RestControllerAdvice
class PersonExceptionHandler {


    @ExceptionHandler(PersonNotFoundException::class)
    fun handleNotFoundException(exc: PersonNotFoundException): ResponseEntity<PersonErrorResponse> {
        val headers = HttpHeaders()
        headers.contentType = MediaType.APPLICATION_JSON
        val resp = PersonErrorResponse(
            HttpStatus.NOT_FOUND.value(),
            exc.message,
            System.currentTimeMillis()
        )
        return ResponseEntity(resp, headers, HttpStatus.NOT_FOUND)
    }

    @ExceptionHandler
    fun handleGenericException(exc : IllegalArgumentException) : ResponseEntity<PersonErrorResponse> {
        val headers = HttpHeaders()
        headers.contentType = MediaType.APPLICATION_JSON
        val personErrorResponse = PersonErrorResponse(HttpStatus.BAD_REQUEST.value(),
            exc.message,
            System.currentTimeMillis())

        return ResponseEntity(personErrorResponse, headers, HttpStatus.BAD_REQUEST)
    }

    @ExceptionHandler
    fun handleGenericException(exc : HttpMessageNotReadableException) : ResponseEntity<PersonErrorResponse> {
        val headers = HttpHeaders()
        headers.contentType = MediaType.APPLICATION_JSON
        val personErrorResponse = PersonErrorResponse(HttpStatus.UNPROCESSABLE_ENTITY.value(),
            "Unreadable Message could not parse body",
            System.currentTimeMillis())

        return ResponseEntity(personErrorResponse, headers, HttpStatus.UNPROCESSABLE_ENTITY)
    }

    @ExceptionHandler
    fun handleGenericException(exc : Throwable) : ResponseEntity<PersonErrorResponse> {
        val personErrorResponse = PersonErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), exc.message,
            System.currentTimeMillis())


        val headers = HttpHeaders()
        headers.contentType = MediaType.APPLICATION_JSON
        return ResponseEntity(personErrorResponse, headers, HttpStatus.INTERNAL_SERVER_ERROR)
    }

    @ExceptionHandler
    fun handleGenericException(exc : StaleObjectStateException) : ResponseEntity<PersonErrorResponse> {
        val personErrorResponse = PersonErrorResponse(HttpStatus.CONFLICT.value(),
            "Object was updated or deleted by another operation",
            System.currentTimeMillis())

        val headers = HttpHeaders()
        headers.contentType = MediaType.APPLICATION_JSON
        return ResponseEntity(personErrorResponse, headers, HttpStatus.CONFLICT)
    }


}

class PersonNotFoundException(message: String) : RuntimeException(message) {
}

class PersonErrorResponse(var status : Int? = null,
                           var message : String? = null,
                           var timeStamp : Long? = null) {

}