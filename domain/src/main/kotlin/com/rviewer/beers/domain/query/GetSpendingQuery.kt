package com.rviewer.beers.domain.query

import com.rviewer.beers.domain.model.Page
import java.util.*

data class GetSpendingQuery(
    val dispenserId: UUID,
    val page: Page,
)
