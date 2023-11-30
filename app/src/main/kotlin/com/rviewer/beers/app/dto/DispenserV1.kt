package com.rviewer.beers.app.dto

import java.util.*

data class DispenserV1(
    val id: UUID,
    val flowVolume: Float,
    val status: String,
)