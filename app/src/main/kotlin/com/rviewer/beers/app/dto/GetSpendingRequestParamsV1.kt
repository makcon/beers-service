package com.rviewer.beers.app.dto

data class GetSpendingRequestParamsV1(
    val pageNumber: Int = 0,
    val pageSize: Int = 20,
)
