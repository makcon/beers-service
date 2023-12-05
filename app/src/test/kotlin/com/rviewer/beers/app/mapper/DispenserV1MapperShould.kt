package com.rviewer.beers.app.mapper

import com.rviewer.beers.app.dto.DispenserV1
import com.rviewer.beers.app.mother.CreateDispenserRequestParamsV1Mother
import com.rviewer.beers.app.mother.DispenserMother
import com.rviewer.beers.domain.command.CreateDispenserCommand
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test

internal class DispenserV1MapperShould {
    
    @Test
    fun `map create request to command`() {
        // given
        val givenRequest = CreateDispenserRequestParamsV1Mother.of()
        
        // when
        val command = givenRequest.toCommand()
        
        // then
        command shouldBe CreateDispenserCommand(
            flowVolume = givenRequest.flowVolume
        )
    }
    
    @Test
    fun `map model to dto`() {
        // given
        val givenModel = DispenserMother.of()
        
        // when
        val dto = givenModel.toDto()
        
        // then
        dto shouldBe DispenserV1(
            id = givenModel.id,
            flowVolume = givenModel.flowVolume,
        )
    }
}