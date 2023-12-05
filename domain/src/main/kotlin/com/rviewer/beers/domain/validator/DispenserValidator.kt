package com.rviewer.beers.domain.validator

import com.rviewer.beers.domain.exception.ModelNotFoundException
import com.rviewer.beers.domain.port.DispenserRepositoryPort
import org.springframework.stereotype.Component
import java.util.*

@Component
class DispenserValidator(
    private val repository: DispenserRepositoryPort,
) {
    
    fun validateIfExists(id: UUID) {
        if (!repository.existsById(id)) {
            throw ModelNotFoundException("Dispenser not found by id: $id")
        }
    }
}