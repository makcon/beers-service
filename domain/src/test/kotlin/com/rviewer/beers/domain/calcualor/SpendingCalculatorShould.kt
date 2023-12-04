package com.rviewer.beers.domain.calcualor

import com.rviewer.beers.domain.config.PriceConfig
import com.rviewer.beers.domain.mother.UsageMother
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import org.junit.jupiter.api.extension.ExtendWith
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import java.math.BigDecimal
import java.time.Instant

@ExtendWith(MockKExtension::class)
internal class SpendingCalculatorShould {
    
    @InjectMockKs
    lateinit var calculator: SpendingCalculator
    
    @MockK
    lateinit var config: PriceConfig
    
    @ParameterizedTest
    @CsvSource(
        value = [
            "12.25,0.064,22,17.24800",
            "10.00,1,5,50.000",
            "1.00,1,1,1.000",
        ]
    )
    fun `calculate total spent for usage`(
        pricePerLitre: BigDecimal,
        flowVolume: Float,
        openedSeconds: Long,
        expectedTotalSpent: BigDecimal,
    ) {
        // given
        val givenOpenedAt = Instant.now()
        val givenClosedAt = givenOpenedAt.plusSeconds(openedSeconds)
        val givenUsage = UsageMother.of(
            totalSpent = null,
            openedAt = givenOpenedAt,
            flowVolume = flowVolume
        )
        every { config.pricePerLitre } returns pricePerLitre
        
        // when
        val totalSpent = calculator.calculate(givenUsage, givenClosedAt)
        
        // then
        totalSpent shouldBe expectedTotalSpent
    }
}