package com.rviewer.beers.domain.port

import com.rviewer.beers.domain.model.Usage
import java.util.*

interface UsageRepositoryPort {
    
    fun create(usage: Usage)
    
    fun update(usage: Usage)
    
    fun findOpened(dispenserId: UUID): Usage?
}