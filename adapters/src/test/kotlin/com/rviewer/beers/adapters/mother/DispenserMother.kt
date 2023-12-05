package com.rviewer.beers.adapters.mother

import com.rviewer.beers.adapters.repository.entity.DispenserEntity
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

object DispenserEntityMother {
    
    fun of(
        id: String = UUID.randomUUID().toString(),
        flowVolume: Float = Random.nextFloat(),
    ) = DispenserEntity(
        id = id,
        flowVolume = flowVolume,
    )
}