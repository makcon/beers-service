package com.rviewer.beers.domain.command.mother

import com.rviewer.beers.domain.command.CreateDispenserCommand
import kotlin.random.Random

object CreateDispenserCommandMother {
    
    fun of(flowVolume: Float = Random.nextFloat()) = CreateDispenserCommand(
        flowVolume = flowVolume
    )
}