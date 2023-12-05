package com.rviewer.beers.domain.command.handler

import com.rviewer.beers.domain.command.CreateDispenserCommand
import com.rviewer.beers.domain.model.Dispenser
import com.rviewer.beers.domain.port.DispenserRepositoryPort
import com.rviewer.beers.domain.utils.IdGenerator
import org.springframework.stereotype.Component

@Component
class CreateDispenserCommandHandler(
    private val repository: DispenserRepositoryPort,
    private val idGenerator: IdGenerator,
) {
    
    fun handle(command: CreateDispenserCommand): Dispenser {
        val dispenser = Dispenser(
            id = idGenerator.dispenser(),
            flowVolume = command.flowVolume,
        )
        
        repository.create(dispenser)
        
        return dispenser
    }
}