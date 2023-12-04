package com.rviewer.beers.app.rest

import com.rviewer.beers.app.dto.CreateDispenserRequestParamsV1
import com.rviewer.beers.app.dto.DispenserV1
import com.rviewer.beers.app.mapper.toCommand
import com.rviewer.beers.app.mapper.toDto
import com.rviewer.beers.domain.command.OpenDispenserCommand
import com.rviewer.beers.domain.command.handler.CreateDispenserCommandHandler
import com.rviewer.beers.domain.command.handler.OpenDispenserCommandHandler
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
@RequestMapping("/v1/dispensers")
class DispenserControllerV1(
    private val createHandler: CreateDispenserCommandHandler,
    private val openHandler: OpenDispenserCommandHandler,
) {
    
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun create(@RequestBody params: CreateDispenserRequestParamsV1): DispenserV1 =
        createHandler.handle(params.toCommand()).toDto()
    
    @PutMapping("/{id}/open")
    @ResponseStatus(HttpStatus.ACCEPTED)
    fun open(@PathVariable id: UUID) = openHandler.handle(OpenDispenserCommand(id))
}