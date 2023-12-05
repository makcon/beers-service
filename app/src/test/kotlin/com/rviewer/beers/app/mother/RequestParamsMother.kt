package com.rviewer.beers.app.mother

import com.rviewer.beers.app.dto.CloseDispenserRequestParamsV1
import com.rviewer.beers.app.dto.CreateDispenserRequestParamsV1
import com.rviewer.beers.app.dto.OpenDispenserRequestParamsV1
import java.time.Instant
import java.time.temporal.ChronoUnit.MILLIS
import kotlin.random.Random

object CreateDispenserRequestParamsV1Mother {
    
    fun of(flowVolume: Float = Random.nextFloat()) = CreateDispenserRequestParamsV1(
        flowVolume = flowVolume
    )
}

object OpenDispenserRequestParamsV1Mother {
    
    fun of(openedAt: Instant = Instant.now().truncatedTo(MILLIS)) = OpenDispenserRequestParamsV1(
        openedAt = openedAt,
    )
}

object CloseDispenserRequestParamsV1Mother {
    
    fun of(closedAt: Instant = Instant.now().truncatedTo(MILLIS)) = CloseDispenserRequestParamsV1(
        closedAt = closedAt,
    )
}