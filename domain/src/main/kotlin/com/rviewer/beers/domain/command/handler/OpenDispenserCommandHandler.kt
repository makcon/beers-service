package com.rviewer.beers.domain.command.handler

import com.rviewer.beers.domain.command.OpenDispenserCommand
import com.rviewer.beers.domain.exception.DispenserInUseException
import com.rviewer.beers.domain.model.Dispenser
import com.rviewer.beers.domain.model.Usage
import com.rviewer.beers.domain.port.DispenserRepositoryPort
import com.rviewer.beers.domain.port.UsageRepositoryPort
import com.rviewer.beers.domain.utils.IdGenerator
import org.springframework.stereotype.Component
import java.time.Instant

@Component
class OpenDispenserCommandHandler(
    private val dispenserRepository: DispenserRepositoryPort,
    private val usageRepository: UsageRepositoryPort,
    private val idGenerator: IdGenerator,
) {
    
    fun handle(command: OpenDispenserCommand) {
        val dispenser = dispenserRepository.findByIdRequired(command.dispenserId)
        
        if (usageRepository.findOpened(command.dispenserId) != null) {
            throw DispenserInUseException(dispenser.id)
        }
    
        createUsage(dispenser, command.openedAt)
    }
    
    private fun createUsage(dispenser: Dispenser, openedAt: Instant) {
        val usage = Usage(
            id = idGenerator.usage(),
            dispenserId = dispenser.id,
            openedAt = openedAt,
            flowVolume = dispenser.flowVolume,
        )
        usageRepository.create(usage)
    }
}