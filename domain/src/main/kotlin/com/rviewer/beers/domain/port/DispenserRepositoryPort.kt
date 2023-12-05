package com.rviewer.beers.domain.port

import com.rviewer.beers.domain.exception.ModelNotFoundException
import com.rviewer.beers.domain.model.Dispenser
import java.util.*

interface DispenserRepositoryPort {
    
    fun create(dispenser: Dispenser)
    
    fun findById(id: UUID): Dispenser?
    
    fun findByIdRequired(id: UUID): Dispenser = findById(id)
        ?: throw ModelNotFoundException("Dispenser not found by id: $id")
    
    fun existsById(id: UUID): Boolean
}