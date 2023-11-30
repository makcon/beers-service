package com.rviewer.beers.domain.model

import java.util.*

data class Dispenser(
    val id: UUID,
    val flowVolume: Float,
    val status: DispenserStatus,
)

enum class DispenserStatus {
    OPENED,
    CLOSED
}