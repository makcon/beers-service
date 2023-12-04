package com.rviewer.beers.domain.calcualor

import com.rviewer.beers.domain.config.PriceConfig
import com.rviewer.beers.domain.model.Usage
import org.springframework.stereotype.Component
import java.math.BigDecimal
import java.time.Duration
import java.time.Instant

@Component
class SpendingCalculator(
    private val config: PriceConfig,
) {
    
    fun calculate(usage: Usage, closedAt: Instant): BigDecimal? {
        val openedSeconds = Duration.between(usage.openedAt, closedAt).toSeconds()
        val litresSpent = usage.flowVolume.toBigDecimal().multiply(openedSeconds.toBigDecimal())
        
        return config.pricePerLitre.multiply(litresSpent)
    }
}
