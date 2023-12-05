package com.rviewer.beers.domain.command

import java.time.Instant
import java.util.*

data class OpenDispenserCommand(
    val dispenserId: UUID,
    val openedAt: Instant,
)
