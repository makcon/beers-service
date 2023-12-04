package com.rviewer.beers.adapters.repository

import com.rviewer.beers.adapters.repository.entity.UsageEntity
import org.springframework.data.jpa.repository.JpaRepository

interface UsageRepository : JpaRepository<UsageEntity, String> {
    
    fun findByDispenserIdAndClosedAtNull(dispenserId: String): UsageEntity?
}