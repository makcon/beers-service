package com.rviewer.beers.domain.exception

import java.util.*

class DispenserInUseException(dispenserId: UUID) : RuntimeException("Dispenser: $dispenserId is already in use")