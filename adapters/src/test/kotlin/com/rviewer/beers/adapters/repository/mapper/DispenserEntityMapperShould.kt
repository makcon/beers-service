package com.rviewer.beers.adapters.repository.mapper

import com.rviewer.beers.adapters.mother.DispenserEntityMother
import com.rviewer.beers.adapters.mother.DispenserMother
import com.rviewer.beers.adapters.repository.entity.DispenserEntity
import com.rviewer.beers.domain.model.Dispenser
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test
import java.util.*

internal class DispenserEntityMapperShould {
    
    @Test
    fun `map model to entity`() {
        // given
        val givenModel = DispenserMother.of()
        
        // when
        val entity = givenModel.toEntity()
        
        // then
        entity shouldBe DispenserEntity(
            id = givenModel.id.toString(),
            flowVolume = givenModel.flowVolume,
            status = givenModel.status,
        )
    }
    
    @Test
    fun `map entity to model`() {
        // given
        val givenModel = DispenserEntityMother.of()
        
        // when
        val entity = givenModel.toModel()
        
        // then
        entity shouldBe Dispenser(
            id = UUID.fromString(givenModel.id),
            flowVolume = givenModel.flowVolume,
            status = givenModel.status,
        )
    }
}