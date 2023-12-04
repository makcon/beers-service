package com.rviewer.beers.domain.port

import com.rviewer.beers.domain.model.Usage

interface UsageRepositoryPort {
    
    fun create(usage: Usage)
}