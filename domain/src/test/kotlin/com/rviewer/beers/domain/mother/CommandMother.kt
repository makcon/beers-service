package com.rviewer.beers.domain.mother

import com.rviewer.beers.domain.command.CloseDispenserCommand
import com.rviewer.beers.domain.command.CreateDispenserCommand
import com.rviewer.beers.domain.command.OpenDispenserCommand
import java.time.Instant
import java.time.temporal.ChronoUnit.MILLIS
import java.util.*
import kotlin.random.Random

object CreateDispenserCommandMother {
    
    fun of(flowVolume: Float = Random.nextFloat()) = CreateDispenserCommand(
        flowVolume = flowVolume
    )
}

object OpenDispenserCommandMother {
    
    fun of(
        dispenserId: UUID = UUID.randomUUID(),
        openedAt: Instant = Instant.now().truncatedTo(MILLIS),
    ) = OpenDispenserCommand(
        dispenserId = dispenserId,
        openedAt = openedAt,
    )
}

object CloseDispenserCommandMother {
    
    fun of(
        dispenserId: UUID = UUID.randomUUID(),
        closedAt: Instant = Instant.now().truncatedTo(MILLIS),
    ) = CloseDispenserCommand(
        dispenserId = dispenserId,
        closedAt = closedAt,
    )
}