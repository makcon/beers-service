package com.rviewer.beers.app.mother

import com.rviewer.beers.app.dto.CreateDispenserRequestParamsV1
import kotlin.random.Random

object CreateDispenserRequestParamsV1Mother {
    
    fun of(flowVolume: Float = Random.nextFloat()) = CreateDispenserRequestParamsV1(
        flowVolume = flowVolume
    )
}