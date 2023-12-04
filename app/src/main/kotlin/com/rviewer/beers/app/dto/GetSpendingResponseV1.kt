package com.rviewer.beers.app.dto

import java.math.BigDecimal

data class GetSpendingResponseV1(
    val totalCount: Long,
    val totalAmount: BigDecimal,
    val usages: List<UsageV1> = listOf(),
)
