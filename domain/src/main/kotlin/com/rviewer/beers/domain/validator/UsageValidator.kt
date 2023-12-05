package com.rviewer.beers.domain.validator

import com.rviewer.beers.domain.exception.ClosedAtInvalidException
import com.rviewer.beers.domain.model.Usage
import org.springframework.stereotype.Component
import java.time.Instant

@Component
class UsageValidator {
    
    fun validateClosedAt(usage: Usage, closedAt: Instant) {
        if (usage.openedAt.isBefore(closedAt)) return
        
        throw ClosedAtInvalidException()
    }
}