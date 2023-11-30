package com.rviewer.beers.domain.port

import com.rviewer.beers.domain.model.Dispenser

interface DispenserRepositoryPort {
    
    fun create(dispenser: Dispenser)
}