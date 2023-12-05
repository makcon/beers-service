package com.rviewer.beers.domain.mother

import com.rviewer.beers.domain.model.Dispenser
import java.util.*
import kotlin.random.Random

object DispenserMother {
    
    fun of(
        id: UUID = UUID.randomUUID(),
        flowVolume: Float = Random.nextFloat(),
    ) = Dispenser(
        id = id,
        flowVolume = flowVolume,
    )
}