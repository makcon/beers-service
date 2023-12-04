package com.rviewer.beers.adapters.repository.mapper

import com.rviewer.beers.adapters.mother.UsageMother
import com.rviewer.beers.adapters.repository.entity.UsageEntity
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test

internal class UsageEntityMapperShould {
    
    @Test
    fun `map model to entity`() {
        // given
        val givenModel = UsageMother.of()
        
        // when
        val entity = givenModel.toEntity()
        
        // then
        entity shouldBe UsageEntity(
            id = givenModel.id.toString(),
            dispenserId = givenModel.dispenserId.toString(),
            openedAt = givenModel.openedAt,
            closedAt = givenModel.closedAt,
            flowVolume = givenModel.flowVolume,
            totalSpent = givenModel.totalSpent,
        )
    }
}