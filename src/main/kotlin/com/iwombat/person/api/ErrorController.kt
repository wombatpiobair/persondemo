package com.iwombat.person.api

import mu.KotlinLogging
import org.slf4j.LoggerFactory
import org.springframework.boot.web.servlet.error.ErrorController
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import javax.servlet.RequestDispatcher

import javax.servlet.http.HttpServletRequest




@RestController
class DefaultErrorController : ErrorController {

    private val logger = KotlinLogging.logger {}

    @RequestMapping("/error", produces = [MediaType.APPLICATION_JSON_VALUE])
    fun handleError(request: HttpServletRequest): PersonErrorResponse {

        val status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE)

        if (status != null) {
            val statusCode = Integer.valueOf(status.toString())
            if (statusCode == HttpStatus.NOT_FOUND.value()) {
                return PersonErrorResponse(HttpStatus.NOT_FOUND.value(), "Endpoint Not Found", System.currentTimeMillis())
            }
        }

        return PersonErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Unknown Server Error", System.currentTimeMillis())
    }
}