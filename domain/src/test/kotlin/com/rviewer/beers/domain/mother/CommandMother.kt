package com.rviewer.beers.domain.mother

import com.rviewer.beers.domain.command.CloseDispenserCommand
import com.rviewer.beers.domain.command.CreateDispenserCommand
import com.rviewer.beers.domain.command.OpenDispenserCommand
import java.util.*
import kotlin.random.Random

object CreateDispenserCommandMother {
    
    fun of(flowVolume: Float = Random.nextFloat()) = CreateDispenserCommand(
        flowVolume = flowVolume
    )
}

object OpenDispenserCommandMother {
    
    fun of(dispenserId: UUID = UUID.randomUUID()) = OpenDispenserCommand(
        dispenserId = dispenserId,
    )
}

object CloseDispenserCommandMother {
    
    fun of(dispenserId: UUID = UUID.randomUUID()) = CloseDispenserCommand(
        dispenserId = dispenserId,
    )
}