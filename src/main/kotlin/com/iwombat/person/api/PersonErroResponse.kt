package com.iwombat.person.api

import org.hibernate.StaleObjectStateException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler

@ControllerAdvice
class PersonExceptionHandler {
    @ExceptionHandler
    fun handleException(exc : PersonNotFoundException) : ResponseEntity<PersonErrorResponse>  {
        val personErrorResponse = PersonErrorResponse(
            HttpStatus.NOT_FOUND.value(),
            exc.message,
            System.currentTimeMillis())

        return ResponseEntity(personErrorResponse, HttpStatus.NOT_FOUND)
    }

    @ExceptionHandler
    fun handleGenericException(exc : IllegalArgumentException) : ResponseEntity<PersonErrorResponse> {
        val personErrorResponse = PersonErrorResponse(HttpStatus.BAD_REQUEST.value(),
            exc.message,
            System.currentTimeMillis())

        return ResponseEntity(personErrorResponse, HttpStatus.BAD_REQUEST)
    }

    @ExceptionHandler
    fun handleGenericException(exc : Exception) : ResponseEntity<PersonErrorResponse> {
        val personErrorResponse = PersonErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(),
            exc.message,
            System.currentTimeMillis())

        return ResponseEntity(personErrorResponse, HttpStatus.INTERNAL_SERVER_ERROR)
    }

    @ExceptionHandler
    fun handleGenericException(exc : StaleObjectStateException) : ResponseEntity<PersonErrorResponse> {
        val personErrorResponse = PersonErrorResponse(HttpStatus.CONFLICT.value(),
            "Object was updated or deleted by another operation",
            System.currentTimeMillis())

        return ResponseEntity(personErrorResponse, HttpStatus.CONFLICT)
    }


}

class PersonNotFoundException(message: String) : RuntimeException(message) {
}

class PersonErrorResponse(var status : Int? = null,
                           var message : String? = null,
                           var timeStamp : Long? = null) {

}