package com.rviewer.beers.adapters

import com.rviewer.beers.adapters.repository.DispenserRepository
import com.rviewer.beers.adapters.repository.mapper.toEntity
import com.rviewer.beers.adapters.repository.mapper.toModel
import com.rviewer.beers.domain.model.Dispenser
import com.rviewer.beers.domain.port.DispenserRepositoryPort
import org.springframework.stereotype.Repository
import java.util.*

@Repository
class DispenserRepositoryAdapter(
    private val repository: DispenserRepository,
) : DispenserRepositoryPort {
    
    override fun create(dispenser: Dispenser) {
        repository.save(dispenser.toEntity())
    }
    
    override fun findById(id: UUID): Dispenser? = repository
        .findById(id.toString())
        .map { it.toModel() }
        .orElse(null)
}