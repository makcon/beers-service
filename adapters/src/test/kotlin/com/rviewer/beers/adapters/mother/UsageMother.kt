package com.rviewer.beers.adapters.mother

import com.rviewer.beers.adapters.repository.entity.UsageEntity
import com.rviewer.beers.domain.model.Usage
import java.math.BigDecimal
import java.time.Instant
import java.time.temporal.ChronoUnit.MILLIS
import java.util.*
import kotlin.random.Random

object UsageMother {
    
    fun of(
        id: UUID = UUID.randomUUID(),
        dispenserId: UUID = UUID.randomUUID(),
        openedAt: Instant = Instant.now().truncatedTo(MILLIS),
        closedAt: Instant? = Instant.now().truncatedTo(MILLIS),
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

object UsageEntityMother {
    
    fun of(
        id: String = UUID.randomUUID().toString(),
        dispenserId: String = UUID.randomUUID().toString(),
        openedAt: Instant = Instant.now().truncatedTo(MILLIS),
        closedAt: Instant? = Instant.now().truncatedTo(MILLIS),
        flowVolume: Float = Random.nextFloat(),
        totalSpent: BigDecimal? = Random.nextDouble().toBigDecimal(),
    ) = UsageEntity(
        id = id,
        dispenserId = dispenserId,
        openedAt = openedAt,
        closedAt = closedAt,
        flowVolume = flowVolume,
        totalSpent = totalSpent,
    )
}