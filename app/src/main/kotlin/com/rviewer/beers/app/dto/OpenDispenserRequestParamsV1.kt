package com.rviewer.beers.app.dto

import java.time.Instant

data class OpenDispenserRequestParamsV1(
    val openedAt: Instant,
)
