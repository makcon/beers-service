package com.rviewer.beers.domain.command.handler

import com.rviewer.beers.domain.calcualor.SpendingCalculator
import com.rviewer.beers.domain.command.CloseDispenserCommand
import com.rviewer.beers.domain.exception.DispenserNotOpenedException
import com.rviewer.beers.domain.model.Usage
import com.rviewer.beers.domain.port.UsageRepositoryPort
import com.rviewer.beers.domain.validator.DispenserValidator
import com.rviewer.beers.domain.validator.UsageValidator
import org.springframework.stereotype.Component
import java.time.Instant

@Component
class CloseDispenserCommandHandler(
    private val dispenserValidator: DispenserValidator,
    private val usageRepository: UsageRepositoryPort,
    private val spendingCalculator: SpendingCalculator,
    private val usageValidator: UsageValidator,
) {
    
    fun handle(command: CloseDispenserCommand) {
        dispenserValidator.validateIfExists(command.dispenserId)
    
        val usage = usageRepository.findOpened(command.dispenserId)
            ?: throw DispenserNotOpenedException(command.dispenserId)
    
        usageValidator.validateClosedAt(usage, command.closedAt)
    
        updateUsage(usage, command.closedAt)
    }
    
    private fun updateUsage(usage: Usage, closedAt: Instant) {
        val updatedUsage = usage.copy(
            closedAt = closedAt,
            totalSpent = spendingCalculator.calculate(usage, closedAt)
        )
        
        usageRepository.update(updatedUsage)
    }
}