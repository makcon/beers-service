package com.rviewer.beers.adapters.repository.mapper

import com.rviewer.beers.adapters.repository.entity.DispenserEntity
import com.rviewer.beers.domain.model.Dispenser
import java.util.*

fun Dispenser.toEntity() = DispenserEntity(
    id = id.toString(),
    flowVolume = flowVolume,
    status = status,
)

fun DispenserEntity.toModel() = Dispenser(
    id = UUID.fromString(id),
    flowVolume = flowVolume,
    status = status,
)