package com.rviewer.beers.app.mother

import com.rviewer.beers.domain.model.Dispenser
import com.rviewer.beers.domain.model.DispenserStatus
import java.util.*
import kotlin.random.Random

object DispenserMother {
    
    fun of(
        id: UUID = UUID.randomUUID(),
        flowVolume: Float = Random.nextFloat(),
        status: DispenserStatus = DispenserStatus.values().random(),
    ) = Dispenser(
        id = id,
        flowVolume = flowVolume,
        status = status
    )
}