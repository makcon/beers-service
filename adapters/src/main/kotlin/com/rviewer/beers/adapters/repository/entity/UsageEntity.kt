package com.rviewer.beers.adapters.repository.entity

import jakarta.persistence.Entity
import jakarta.persistence.Id
import java.math.BigDecimal
import java.time.Instant

@Entity(name = "usages")
data class UsageEntity(
    @field:Id
    val id: String,
    val dispenserId: String,
    val openedAt: Instant,
    val closedAt: Instant? = null,
    val flowVolume: Float,
    val totalSpent: BigDecimal? = null,
)
