package com.rviewer.beers.domain.query.handler

import com.rviewer.beers.domain.calcualor.SpendingCalculator
import com.rviewer.beers.domain.exception.ModelNotFoundException
import com.rviewer.beers.domain.model.GetSpendingResult
import com.rviewer.beers.domain.model.Usage
import com.rviewer.beers.domain.mother.GetSpendingQueryMother
import com.rviewer.beers.domain.mother.PageResultMother
import com.rviewer.beers.domain.mother.UsageMother
import com.rviewer.beers.domain.port.UsageRepositoryPort
import com.rviewer.beers.domain.validator.DispenserValidator
import io.kotest.matchers.shouldBe
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.junit5.MockKExtension
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import java.math.BigDecimal
import java.time.Clock
import java.time.Instant
import kotlin.random.Random

@ExtendWith(MockKExtension::class)
internal class GetSpendingQueryHandlerShould {
    
    @InjectMockKs
    lateinit var handler: GetSpendingQueryHandler
    
    @MockK
    lateinit var usageRepository: UsageRepositoryPort
    
    @MockK
    lateinit var spendingCalculator: SpendingCalculator
    
    @MockK
    lateinit var clock: Clock
    
    @RelaxedMockK
    lateinit var dispenserValidator: DispenserValidator
    
    private val foundTotalAmount: BigDecimal = Random.nextDouble().toBigDecimal()
    private val calculatedUsageSpent: BigDecimal = Random.nextDouble().toBigDecimal()
    private val expectedNow = Instant.now()
    private val givenQuery = GetSpendingQueryMother.of()
    
    @BeforeEach
    internal fun setUp() {
        every { usageRepository.getTotalAmountByDispenserId(any()) } returns foundTotalAmount
        every { spendingCalculator.calculate(any(), any()) } returns calculatedUsageSpent
        every { clock.instant() } returns expectedNow
    }
    
    @AfterEach
    internal fun tearDown() {
        verify(exactly = 1) { dispenserValidator.validateIfExists(givenQuery.dispenserId) }
    
        confirmVerified(
            usageRepository,
            spendingCalculator,
            clock,
            dispenserValidator
        )
    }
    
    @Test
    fun `return empty response when there are no usages`() {
        // given
        val foundPageResult = PageResultMother.of<Usage>(items = listOf())
        every { usageRepository.findLatestByDispenserId(any(), any()) } returns foundPageResult
        
        // when
        val result = handler.handle(givenQuery)
        
        // then
        result shouldBe GetSpendingResult(
            totalCount = 0L,
            totalAmount = BigDecimal.ZERO,
            usages = listOf(),
        )
        verify(exactly = 1) { usageRepository.findLatestByDispenserId(givenQuery.dispenserId, givenQuery.page) }
    }
    
    @Test
    fun `return response when all usages are closed`() {
        // given
        val storedUsage = UsageMother.of(closedAt = Instant.now())
        val storedUsage2 = UsageMother.of(closedAt = Instant.now())
        val foundPageResult = PageResultMother.of(items = listOf(storedUsage, storedUsage2))
        every { usageRepository.findLatestByDispenserId(any(), any()) } returns foundPageResult
        
        // when
        val result = handler.handle(givenQuery)
        
        // then
        result shouldBe GetSpendingResult(
            totalCount = foundPageResult.totalCount,
            totalAmount = foundTotalAmount,
            usages = listOf(storedUsage, storedUsage2),
        )
        verify(exactly = 1) { usageRepository.findLatestByDispenserId(givenQuery.dispenserId, givenQuery.page) }
        verify(exactly = 0) { spendingCalculator.calculate(any(), any()) }
        verify(exactly = 1) { usageRepository.getTotalAmountByDispenserId(givenQuery.dispenserId) }
    }
    
    @Test
    fun `return response with calculated usage when one usage aren't closed`() {
        // given
        val storedUsage = UsageMother.of(closedAt = null)
        val storedUsage2 = UsageMother.of(closedAt = Instant.now())
        val foundPageResult = PageResultMother.of(items = listOf(storedUsage, storedUsage2))
        every { usageRepository.findLatestByDispenserId(any(), any()) } returns foundPageResult
        
        // when
        val result = handler.handle(givenQuery)
        
        // then
        result shouldBe GetSpendingResult(
            totalCount = foundPageResult.totalCount,
            totalAmount = foundTotalAmount.plus(calculatedUsageSpent),
            usages = listOf(
                storedUsage.copy(totalSpent = calculatedUsageSpent),
                storedUsage2
            ),
        )
        verify(exactly = 1) { usageRepository.findLatestByDispenserId(givenQuery.dispenserId, givenQuery.page) }
        verify(exactly = 1) { spendingCalculator.calculate(storedUsage, expectedNow) }
        verify(exactly = 1) { usageRepository.getTotalAmountByDispenserId(givenQuery.dispenserId) }
        verify(exactly = 1) { clock.instant() }
    }
    
    @Test
    fun `throw validation exception when dispenser validator throws`() {
        // given
        val expectedException = mockk<ModelNotFoundException>()
        every { dispenserValidator.validateIfExists(any()) } throws expectedException
        
        // when
        val exception = assertThrows<ModelNotFoundException> { handler.handle(givenQuery) }
        
        // then
        exception shouldBe expectedException
    }
}