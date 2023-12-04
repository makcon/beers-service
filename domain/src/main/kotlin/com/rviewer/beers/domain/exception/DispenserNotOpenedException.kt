package com.rviewer.beers.domain.exception

import java.util.*

class DispenserNotOpenedException(dispenserId: UUID) : RuntimeException("Dispenser $dispenserId is not opened")