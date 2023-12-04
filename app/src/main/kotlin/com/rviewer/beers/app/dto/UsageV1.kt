package com.rviewer.beers.app.dto

import java.math.BigDecimal
import java.time.Instant

data class UsageV1(
    val openedAt: Instant,
    val closedAt: Instant? = null,
    val flowVolume: Float,
    val totalSpent: BigDecimal? = null,
)
