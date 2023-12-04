package com.rviewer.beers.domain.model

import java.math.BigDecimal

data class GetSpendingResult(
    val totalCount: Long = 0L,
    val totalAmount: BigDecimal = BigDecimal.ZERO,
    val usages: List<Usage> = listOf(),
)
