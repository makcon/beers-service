package com.rviewer.beers.app.dto

data class ErrorV1(
    val code: String,
    val message: String,
    val attributes: Map<String, Any?> = mapOf(),
)

object ErrorCode {
    
    const val NOT_FOUND = "NotFound"
    const val ALREADY_OPENED = "AlreadyOpened"
}
