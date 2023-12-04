package com.rviewer.beers.domain.mother

import com.rviewer.beers.domain.model.PageResult
import kotlin.random.Random

object PageResultMother {
    
    fun <T> of(
        totalCount: Long = Random.nextLong(),
        items: List<T> = listOf(),
    ) = PageResult(
        totalCount = totalCount,
        items = items
    )
}