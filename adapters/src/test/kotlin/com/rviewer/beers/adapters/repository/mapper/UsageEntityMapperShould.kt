package com.rviewer.beers.adapters.repository.mapper

import com.rviewer.beers.adapters.mother.UsageEntityMother
import com.rviewer.beers.adapters.mother.UsageMother
import com.rviewer.beers.adapters.repository.entity.UsageEntity
import com.rviewer.beers.domain.model.Usage
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test
import java.util.*

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
    
    @Test
    fun `map entity to model`() {
        // given
        val givenEntity = UsageEntityMother.of()
        
        // when
        val entity = givenEntity.toModel()
        
        // then
        entity shouldBe Usage(
            id = UUID.fromString(givenEntity.id),
            dispenserId = UUID.fromString(givenEntity.dispenserId),
            openedAt = givenEntity.openedAt,
            closedAt = givenEntity.closedAt,
            flowVolume = givenEntity.flowVolume,
            totalSpent = givenEntity.totalSpent,
        )
    }
}