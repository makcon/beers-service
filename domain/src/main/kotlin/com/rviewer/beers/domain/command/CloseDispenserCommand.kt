package com.rviewer.beers.domain.command

import java.util.*

data class CloseDispenserCommand(
    val dispenserId: UUID,
)
