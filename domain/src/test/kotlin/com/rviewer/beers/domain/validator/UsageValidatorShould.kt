package com.rviewer.beers.domain.validator

import com.rviewer.beers.domain.exception.ClosedAtInvalidException
import com.rviewer.beers.domain.mother.UsageMother
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.junit5.MockKExtension
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource

@ExtendWith(MockKExtension::class)
internal class UsageValidatorShould {
    
    @InjectMockKs
    lateinit var validator: UsageValidator
    
    @Test
    fun `pass when closedAt is after openedAt`() {
        // given
        val givenUsage = UsageMother.of()
        val givenClosedAt = givenUsage.openedAt.plusSeconds(1)
        
        // when - then
        assertDoesNotThrow { validator.validateClosedAt(givenUsage, givenClosedAt) }
    }
    
    @ParameterizedTest
    @ValueSource(longs = [0, 1])
    fun `throw exception when closedAt is equal or before openedAt`(secondsToSubtract: Long) {
        // given
        val givenUsage = UsageMother.of()
        val givenClosedAt = givenUsage.openedAt.minusSeconds(secondsToSubtract)
        
        // when - then
        assertThrows<ClosedAtInvalidException> { validator.validateClosedAt(givenUsage, givenClosedAt) }
    }
}