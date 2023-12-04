package com.rviewer.beers.adapters

import com.rviewer.beers.adapters.repository.UsageRepository
import com.rviewer.beers.adapters.repository.mapper.toEntity
import com.rviewer.beers.adapters.repository.mapper.toModel
import com.rviewer.beers.domain.model.Page
import com.rviewer.beers.domain.model.PageResult
import com.rviewer.beers.domain.model.Usage
import com.rviewer.beers.domain.port.UsageRepositoryPort
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Repository
import java.math.BigDecimal
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
    
    override fun findLatestByDispenserId(dispenserId: UUID, page: Page): PageResult<Usage> = repository
        .findByDispenserId(
            dispenserId = dispenserId.toString(),
            page = PageRequest.of(
                page.number,
                page.size,
                Sort.by(Sort.Order.desc(Usage::openedAt.name))
            )
        )
        .toModel()
    
    override fun getTotalAmountByDispenserId(dispenserId: UUID): BigDecimal =
        repository.sumTotalSpent(dispenserId.toString())
}