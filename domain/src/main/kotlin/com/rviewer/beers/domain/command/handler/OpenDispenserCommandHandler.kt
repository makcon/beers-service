package com.rviewer.beers.domain.command.handler

import com.rviewer.beers.domain.command.OpenDispenserCommand
import com.rviewer.beers.domain.exception.DispenserInUseException
import com.rviewer.beers.domain.model.Dispenser
import com.rviewer.beers.domain.model.DispenserStatus
import com.rviewer.beers.domain.model.Usage
import com.rviewer.beers.domain.port.DispenserRepositoryPort
import com.rviewer.beers.domain.port.UsageRepositoryPort
import com.rviewer.beers.domain.utils.IdGenerator
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import java.time.Clock

@Component
class OpenDispenserCommandHandler(
    private val dispenserRepository: DispenserRepositoryPort,
    private val usageRepository: UsageRepositoryPort,
    private val idGenerator: IdGenerator,
    private val clock: Clock,
) {
    
    @Transactional
    fun handle(command: OpenDispenserCommand) {
        val dispenser = dispenserRepository.findByIdRequired(command.dispenserId)
        
        if (dispenser.status == DispenserStatus.OPENED) {
            throw DispenserInUseException(dispenser.id)
        }
        
        createUsage(dispenser)
        updateStatus(dispenser)
    }
    
    private fun createUsage(dispenser: Dispenser) {
        val usage = Usage(
            id = idGenerator.usage(),
            dispenserId = dispenser.id,
            openedAt = clock.instant(),
            flowVolume = dispenser.flowVolume,
        )
        usageRepository.create(usage)
    }
    
    private fun updateStatus(dispenser: Dispenser) {
        val updated = dispenser.copy(
            status = DispenserStatus.OPENED
        )
        dispenserRepository.update(updated)
    }
}