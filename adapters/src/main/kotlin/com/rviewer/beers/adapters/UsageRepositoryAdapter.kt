package com.rviewer.beers.adapters

import com.rviewer.beers.adapters.repository.UsageRepository
import com.rviewer.beers.adapters.repository.mapper.toEntity
import com.rviewer.beers.domain.model.Usage
import com.rviewer.beers.domain.port.UsageRepositoryPort
import org.springframework.stereotype.Repository

@Repository
class UsageRepositoryAdapter(
    private val repository: UsageRepository,
) : UsageRepositoryPort {
    
    override fun create(usage: Usage) {
        repository.save(usage.toEntity())
    }
}