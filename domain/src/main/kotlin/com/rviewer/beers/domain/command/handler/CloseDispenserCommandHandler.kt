package com.rviewer.beers.domain.command.handler

import com.rviewer.beers.domain.calcualor.SpendingCalculator
import com.rviewer.beers.domain.command.CloseDispenserCommand
import com.rviewer.beers.domain.exception.DispenserNotOpenedException
import com.rviewer.beers.domain.model.Usage
import com.rviewer.beers.domain.port.DispenserRepositoryPort
import com.rviewer.beers.domain.port.UsageRepositoryPort
import org.springframework.stereotype.Component
import java.time.Clock
import java.time.Instant

@Component
class CloseDispenserCommandHandler(
    private val dispenserRepository: DispenserRepositoryPort,
    private val usageRepository: UsageRepositoryPort,
    private val clock: Clock,
    private val spendingCalculator: SpendingCalculator,
) {
    
    fun handle(command: CloseDispenserCommand) {
        val closedAt = clock.instant()
    
        val dispenser = dispenserRepository.findByIdRequired(command.dispenserId)
        val usage = usageRepository.findOpened(dispenser.id)
            ?: throw DispenserNotOpenedException(dispenser.id)
    
        updateUsage(usage, closedAt)
    }
    
    private fun updateUsage(usage: Usage, closedAt: Instant) {
        val updatedUsage = usage.copy(
            closedAt = closedAt,
            totalSpent = spendingCalculator.calculate(usage, closedAt)
        )
        
        usageRepository.update(updatedUsage)
    }
}