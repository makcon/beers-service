package com.rviewer.beers.app.mapper

import com.rviewer.beers.app.dto.CreateDispenserRequestParamsV1
import com.rviewer.beers.app.dto.DispenserV1
import com.rviewer.beers.domain.command.CreateDispenserCommand
import com.rviewer.beers.domain.model.Dispenser

fun CreateDispenserRequestParamsV1.toCommand() = CreateDispenserCommand(
    flowVolume = flowVolume,
)

fun Dispenser.toDto() = DispenserV1(
    id = id,
    flowVolume = flowVolume,
    status = status.name
)