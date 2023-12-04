package com.rviewer.beers.adapters

import com.rviewer.beers.adapters.repository.UsageRepository
import com.rviewer.beers.adapters.repository.mapper.toEntity
import com.rviewer.beers.adapters.repository.mapper.toModel
import com.rviewer.beers.domain.model.Usage
import com.rviewer.beers.domain.port.UsageRepositoryPort
import org.springframework.stereotype.Repository
import java.util.*

@Repository
class UsageRepositoryAdapter(
    private val repository: UsageRepository,
) : UsageRepositoryPort {
    
    override fun create(usage: Usage) {
        repository.save(usage.toEntity())
    }
    
    override fun update(usage: Usage) {
        repository.save(usage.toEntity())
    }
    
    override fun findOpened(dispenserId: UUID): Usage? =
        repository.findByDispenserIdAndClosedAtNull(dispenserId.toString())?.toModel()
}