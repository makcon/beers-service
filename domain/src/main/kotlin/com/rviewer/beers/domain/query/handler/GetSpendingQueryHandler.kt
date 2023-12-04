package com.rviewer.beers.domain.query.handler

import com.rviewer.beers.domain.calcualor.SpendingCalculator
import com.rviewer.beers.domain.model.GetSpendingResult
import com.rviewer.beers.domain.model.Usage
import com.rviewer.beers.domain.port.UsageRepositoryPort
import com.rviewer.beers.domain.query.GetSpendingQuery
import org.springframework.stereotype.Component
import java.math.BigDecimal
import java.time.Clock

@Component
class GetSpendingQueryHandler(
    private val usageRepository: UsageRepositoryPort,
    private val spendingCalculator: SpendingCalculator,
    private val clock: Clock,
) {
    
    fun handle(query: GetSpendingQuery): GetSpendingResult {
        val page = usageRepository.findLatestByDispenserId(query.dispenserId, query.page)
        
        if (page.items.isEmpty()) return GetSpendingResult()
        
        val totalAmounts = mutableListOf(usageRepository.getTotalAmountByDispenserId(query.dispenserId))
        val usages = page.items.map { calculateOpened(it, totalAmounts) }
        
        return GetSpendingResult(
            totalCount = page.totalCount,
            totalAmount = totalAmounts.sumOf { it },
            usages = usages
        )
    }
    
    private fun calculateOpened(usage: Usage, totalAmounts: MutableList<BigDecimal>): Usage {
        if (usage.closedAt != null) return usage
        
        val totalSpent = spendingCalculator.calculate(usage, clock.instant())
        totalAmounts.add(totalSpent)
        
        return usage.copy(totalSpent = totalSpent)
    }
}