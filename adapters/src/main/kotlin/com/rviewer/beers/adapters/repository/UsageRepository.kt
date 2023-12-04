package com.rviewer.beers.adapters.repository

import com.rviewer.beers.adapters.repository.entity.UsageEntity
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import java.math.BigDecimal

interface UsageRepository : JpaRepository<UsageEntity, String> {
    
    fun findByDispenserIdAndClosedAtNull(dispenserId: String): UsageEntity?
    
    fun findByDispenserId(dispenserId: String, page: Pageable): Page<UsageEntity>
    
    @Query("SELECT sum(e.totalSpent) FROM usages e WHERE e.dispenserId = ?1")
    fun sumTotalSpent(dispenserId: String): BigDecimal
}