package com.rviewer.beers.domain.command

import java.time.Instant
import java.util.*

data class CloseDispenserCommand(
    val dispenserId: UUID,
    val closedAt: Instant,
)
