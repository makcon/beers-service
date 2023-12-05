package com.rviewer.beers.app.dto

data class ErrorV1(
    val code: String,
    val message: String,
    val attributes: Map<String, Any?> = mapOf(),
)

object ErrorCode {
    
    const val INTERNAL_ERROR = "InternalError"
    const val NOT_FOUND = "NotFound"
    const val ALREADY_OPENED = "AlreadyOpened"
    const val ALREADY_CLOSED = "AlreadyClosed"
    const val CLOSED_AT_INVALID = "ClosedAtInvalid"
}
