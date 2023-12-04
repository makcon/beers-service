package com.rviewer.beers.domain.model

import java.math.BigDecimal
import java.time.Instant
import java.util.*

data class Usage(
    val id: UUID,
    val dispenserId: UUID,
    val openedAt: Instant,
    val closedAt: Instant? = null,
    val flowVolume: Float,
    val totalSpent: BigDecimal? = null,
)
