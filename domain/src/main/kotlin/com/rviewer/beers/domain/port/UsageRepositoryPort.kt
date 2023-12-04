package com.rviewer.beers.domain.port

import com.rviewer.beers.domain.model.Page
import com.rviewer.beers.domain.model.PageResult
import com.rviewer.beers.domain.model.Usage
import java.math.BigDecimal
import java.util.*

interface UsageRepositoryPort {
    
    fun create(usage: Usage)
    
    fun update(usage: Usage)
    
    fun findOpened(dispenserId: UUID): Usage?
    
    fun findLatestByDispenserId(dispenserId: UUID, page: Page): PageResult<Usage>
    
    fun getTotalAmountByDispenserId(dispenserId: UUID): BigDecimal
}