package com.rviewer.beers.app.rest

import com.rviewer.beers.app.dto.ErrorCode
import com.rviewer.beers.app.dto.ErrorV1
import com.rviewer.beers.domain.exception.ClosedAtInvalidException
import com.rviewer.beers.domain.exception.DispenserInUseException
import com.rviewer.beers.domain.exception.DispenserNotOpenedException
import com.rviewer.beers.domain.exception.ModelNotFoundException
import mu.KotlinLogging
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class RestExceptionHandler {
    
    private val log = KotlinLogging.logger { }
    
    @ExceptionHandler(Exception::class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    fun handle(exception: Exception): ErrorV1 = createError(
        code = ErrorCode.INTERNAL_ERROR,
        exception = exception
    )
    
    @ExceptionHandler(ModelNotFoundException::class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    fun handle(exception: ModelNotFoundException): ErrorV1 = createError(
        code = ErrorCode.NOT_FOUND,
        exception = exception
    )
    
    @ExceptionHandler(DispenserInUseException::class)
    @ResponseStatus(HttpStatus.CONFLICT)
    fun handle(exception: DispenserInUseException): ErrorV1 = createError(
        code = ErrorCode.ALREADY_OPENED,
        exception = exception
    )
    
    @ExceptionHandler(DispenserNotOpenedException::class)
    @ResponseStatus(HttpStatus.CONFLICT)
    fun handle(exception: DispenserNotOpenedException): ErrorV1 = createError(
        code = ErrorCode.ALREADY_CLOSED,
        exception = exception
    )
    
    @ExceptionHandler(ClosedAtInvalidException::class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    fun handle(exception: ClosedAtInvalidException): ErrorV1 = createError(
        code = ErrorCode.CLOSED_AT_INVALID,
        exception = exception
    )
    
    private fun createError(
        code: String,
        exception: Exception,
        attributes: Map<String, Any?> = mapOf(),
    ): ErrorV1 {
        log.warn { exception.message }
        return ErrorV1(
            code = code,
            message = exception.message ?: "Unknown message",
            attributes = attributes
        )
    }
}