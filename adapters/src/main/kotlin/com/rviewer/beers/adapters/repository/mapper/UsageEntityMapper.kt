package com.rviewer.beers.adapters.repository.mapper

import com.rviewer.beers.adapters.repository.entity.UsageEntity
import com.rviewer.beers.domain.model.Usage

fun Usage.toEntity() = UsageEntity(
    id = id.toString(),
    dispenserId = dispenserId.toString(),
    openedAt = openedAt,
    closedAt = closedAt,
    flowVolume = flowVolume,
    totalSpent = totalSpent,
)