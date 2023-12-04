package com.rviewer.beers.adapters.repository.mapper

import com.rviewer.beers.adapters.repository.entity.UsageEntity
import com.rviewer.beers.domain.model.PageResult
import com.rviewer.beers.domain.model.Usage
import org.springframework.data.domain.Page
import java.util.*

fun Usage.toEntity() = UsageEntity(
    id = id.toString(),
    dispenserId = dispenserId.toString(),
    openedAt = openedAt,
    closedAt = closedAt,
    flowVolume = flowVolume,
    totalSpent = totalSpent,
)

fun UsageEntity.toModel() = Usage(
    id = UUID.fromString(id),
    dispenserId = UUID.fromString(dispenserId),
    openedAt = openedAt,
    closedAt = closedAt,
    flowVolume = flowVolume,
    totalSpent = totalSpent,
)

fun Page<UsageEntity>.toModel() = PageResult(
    totalCount = totalElements,
    items = content.map { it.toModel() },
)