package com.rviewer.beers.app.dto

import java.time.Instant

data class CloseDispenserRequestParamsV1(
    val closedAt: Instant,
)
