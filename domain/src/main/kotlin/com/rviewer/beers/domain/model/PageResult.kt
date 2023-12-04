package com.rviewer.beers.domain.model

data class PageResult<T>(
    val totalCount: Long,
    val items: List<T>,
)
