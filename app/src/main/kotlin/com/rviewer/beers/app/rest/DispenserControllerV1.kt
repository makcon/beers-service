package com.rviewer.beers.app.rest

import com.rviewer.beers.app.dto.*
import com.rviewer.beers.app.mapper.toCommand
import com.rviewer.beers.app.mapper.toDto
import com.rviewer.beers.app.mapper.toQuery
import com.rviewer.beers.domain.command.handler.CloseDispenserCommandHandler
import com.rviewer.beers.domain.command.handler.CreateDispenserCommandHandler
import com.rviewer.beers.domain.command.handler.OpenDispenserCommandHandler
import com.rviewer.beers.domain.query.handler.GetSpendingQueryHandler
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
@RequestMapping("/v1/dispensers")
class DispenserControllerV1(
    private val createHandler: CreateDispenserCommandHandler,
    private val openHandler: OpenDispenserCommandHandler,
    private val closeHandler: CloseDispenserCommandHandler,
    private val getSpendingHandler: GetSpendingQueryHandler,
) {
    
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun create(@RequestBody params: CreateDispenserRequestParamsV1): DispenserV1 =
        createHandler.handle(params.toCommand()).toDto()
    
    @PutMapping("/{id}/open")
    @ResponseStatus(HttpStatus.ACCEPTED)
    fun open(@PathVariable id: UUID, @RequestBody params: OpenDispenserRequestParamsV1) =
        openHandler.handle(params.toCommand(id))
    
    @PutMapping("/{id}/close")
    @ResponseStatus(HttpStatus.ACCEPTED)
    fun close(@PathVariable id: UUID, @RequestBody params: CloseDispenserRequestParamsV1) =
        closeHandler.handle(params.toCommand(id))
    
    @GetMapping("{id}/spending")
    fun getSpending(@PathVariable id: UUID, params: GetSpendingRequestParamsV1): GetSpendingResponseV1 =
        getSpendingHandler.handle(params.toQuery(id)).toDto()
}