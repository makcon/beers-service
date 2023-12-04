package com.rviewer.beers.domain.mother

import com.rviewer.beers.domain.model.Page
import com.rviewer.beers.domain.query.GetSpendingQuery
import java.util.*
import kotlin.random.Random

object GetSpendingQueryMother {
    
    fun of(
        dispenserId: UUID = UUID.randomUUID(),
        page: Page = Page(Random.nextInt(), Random.nextInt()),
    ) = GetSpendingQuery(
        dispenserId = dispenserId,
        page = page
    )
}