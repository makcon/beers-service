package com.rviewer.beers.domain.command.handler

import com.rviewer.beers.domain.calcualor.SpendingCalculator
import com.rviewer.beers.domain.command.CloseDispenserCommand
import com.rviewer.beers.domain.exception.DispenserNotOpenedException
import com.rviewer.beers.domain.model.Dispenser
import com.rviewer.beers.domain.model.DispenserStatus
import com.rviewer.beers.domain.port.DispenserRepositoryPort
import com.rviewer.beers.domain.port.UsageRepositoryPort
import org.springframework.stereotype.Component
import java.time.Clock

@Component
class CloseDispenserCommandHandler(
    private val dispenserRepository: DispenserRepositoryPort,
    private val usageRepository: UsageRepositoryPort,
    private val clock: Clock,
    private val spendingCalculator: SpendingCalculator,
) {
    
    fun handle(command: CloseDispenserCommand) {
        val dispenser = dispenserRepository.findByIdRequired(command.dispenserId)
        
        updateUsage(dispenser)
        updateStatus(dispenser)
    }
    
    private fun updateUsage(dispenser: Dispenser) {
        val usage = usageRepository.findOpened(dispenser.id)
            ?: throw DispenserNotOpenedException(dispenser.id)
        
        val closedAt = clock.instant()
        val updatedUsage = usage.copy(
            closedAt = closedAt,
            totalSpent = spendingCalculator.calculate(usage, closedAt)
        )
        
        usageRepository.update(updatedUsage)
    }
    
    private fun updateStatus(dispenser: Dispenser) {
        val updated = dispenser.copy(
            status = DispenserStatus.CLOSED
        )
        dispenserRepository.update(updated)
    }
}