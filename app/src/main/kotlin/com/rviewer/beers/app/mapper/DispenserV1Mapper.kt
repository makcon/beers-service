package com.rviewer.beers.app.mapper

import com.rviewer.beers.app.dto.*
import com.rviewer.beers.app.utils.withPrecision
import com.rviewer.beers.domain.command.CreateDispenserCommand
import com.rviewer.beers.domain.model.Dispenser
import com.rviewer.beers.domain.model.GetSpendingResult
import com.rviewer.beers.domain.model.Page
import com.rviewer.beers.domain.model.Usage
import com.rviewer.beers.domain.query.GetSpendingQuery
import java.util.*

fun CreateDispenserRequestParamsV1.toCommand() = CreateDispenserCommand(
    flowVolume = flowVolume,
)

fun Dispenser.toDto() = DispenserV1(
    id = id,
    flowVolume = flowVolume,
    status = status.name
)

fun GetSpendingRequestParamsV1.toQuery(dispenserId: UUID) = GetSpendingQuery(
    dispenserId = dispenserId,
    page = Page(
        number = pageNumber,
        size = pageSize,
    ),
)

fun GetSpendingResult.toDto() = GetSpendingResponseV1(
    totalCount = totalCount,
    totalAmount = totalAmount.withPrecision(),
    usages = usages.map { it.toDto() }
)

fun Usage.toDto() = UsageV1(
    openedAt = openedAt,
    closedAt = closedAt,
    flowVolume = flowVolume,
    totalSpent = totalSpent?.withPrecision(),
)