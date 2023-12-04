package com.rviewer.beers.domain.command

import java.util.*

data class OpenDispenserCommand(
    val dispenserId: UUID,
)
