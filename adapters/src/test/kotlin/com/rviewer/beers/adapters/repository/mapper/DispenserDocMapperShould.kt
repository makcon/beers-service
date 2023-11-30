package com.rviewer.beers.adapters.repository.mapper

import com.rviewer.beers.adapters.mother.DispenserMother
import com.rviewer.beers.adapters.repository.entity.DispenserEntity
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test

internal class DispenserDocMapperShould {
    
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
}