package com.rviewer.beers.app.mother

import com.rviewer.beers.domain.model.Usage
import java.math.BigDecimal
import java.time.Instant
import java.util.*
import kotlin.random.Random

object UsageMother {
    
    fun of(
        id: UUID = UUID.randomUUID(),
        dispenserId: UUID = UUID.randomUUID(),
        openedAt: Instant = Instant.now(),
        closedAt: Instant? = Instant.now(),
        flowVolume: Float = Random.nextFloat(),
        totalSpent: BigDecimal? = Random.nextDouble().toBigDecimal(),
    ) = Usage(
        id = id,
        dispenserId = dispenserId,
        openedAt = openedAt,
        closedAt = closedAt,
        flowVolume = flowVolume,
        totalSpent = totalSpent,
    )
}