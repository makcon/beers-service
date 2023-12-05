package com.rviewer.beers.domain.command.handler

import com.rviewer.beers.domain.calcualor.SpendingCalculator
import com.rviewer.beers.domain.exception.DispenserInUseException
import com.rviewer.beers.domain.exception.DispenserNotOpenedException
import com.rviewer.beers.domain.model.Usage
import com.rviewer.beers.domain.mother.CloseDispenserCommandMother
import com.rviewer.beers.domain.mother.DispenserMother
import com.rviewer.beers.domain.mother.UsageMother
import com.rviewer.beers.domain.port.DispenserRepositoryPort
import com.rviewer.beers.domain.port.UsageRepositoryPort
import io.kotest.matchers.shouldBe
import io.mockk.*
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.junit5.MockKExtension
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
internal class CloseDispenserCommandHandlerShould {
    
    @InjectMockKs
    lateinit var handler: CloseDispenserCommandHandler
    
    @RelaxedMockK
    lateinit var dispenserRepository: DispenserRepositoryPort
    
    @RelaxedMockK
    lateinit var usageRepository: UsageRepositoryPort
    
    @MockK
    lateinit var clock: Clock
    
    @MockK
    lateinit var spendingCalculator: SpendingCalculator
    
    private val storedDispenser = DispenserMother.of()
    private val usageSlot = slot<Usage>()
    private val expectedClosedAt = Instant.now()
    
    @BeforeEach
    internal fun setUp() {
        every { clock.instant() } returns expectedClosedAt
        every { dispenserRepository.findByIdRequired(any()) } returns storedDispenser
    }
    
    @AfterEach
    internal fun tearDown() {
        verify(exactly = 1) { clock.instant() }
    
        confirmVerified(
            dispenserRepository,
            usageRepository,
            clock,
            spendingCalculator,
        )
    }
    
    @Test
    fun `does nothing when dispenser not found`() {
        // given
        val givenCommand = CloseDispenserCommandMother.of()
        every { dispenserRepository.findByIdRequired(any()) } throws mockk()
        
        // when
        assertThrows<DispenserInUseException> { handler.handle(givenCommand) }
        
        // then
        verify(exactly = 1) { dispenserRepository.findByIdRequired(givenCommand.dispenserId) }
    }
    
    @Test
    fun `throw not opened exception when opened usage not found`() {
        // given
        val givenCommand = CloseDispenserCommandMother.of(
            dispenserId = storedDispenser.id,
        )
        every { usageRepository.findOpened(any()) } returns null
        
        // when
        assertThrows<DispenserNotOpenedException> { handler.handle(givenCommand) }
        
        // then
        verify(exactly = 1) { dispenserRepository.findByIdRequired(givenCommand.dispenserId) }
        verify(exactly = 1) { usageRepository.findOpened(givenCommand.dispenserId) }
    }
    
    @Test
    fun `update usage when opened usage found`() {
        // given
        val storedUsage = UsageMother.of(
            dispenserId = storedDispenser.id,
            closedAt = null,
            totalSpent = null
        )
        val givenCommand = CloseDispenserCommandMother.of(
            dispenserId = storedDispenser.id,
        )
        val calculatedAmount = Random.nextDouble().toBigDecimal()
        every { usageRepository.findOpened(any()) } returns storedUsage
        every { spendingCalculator.calculate(any(), any()) } returns calculatedAmount
        
        // when
        handler.handle(givenCommand)
        
        // then
        verify(exactly = 1) { dispenserRepository.findByIdRequired(givenCommand.dispenserId) }
        verify(exactly = 1) { usageRepository.findOpened(givenCommand.dispenserId) }
        verify(exactly = 1) { spendingCalculator.calculate(storedUsage, expectedClosedAt) }
        verifyUsageUpdated(storedUsage, expectedClosedAt, calculatedAmount)
    }
    
    private fun verifyUsageUpdated(
        storedUsage: Usage,
        expectedClosedAt: Instant?,
        calculatedAmount: BigDecimal,
    ) {
        verify(exactly = 1) { usageRepository.update(capture(usageSlot)) }
        usageSlot.captured shouldBe storedUsage.copy(
            closedAt = expectedClosedAt,
            totalSpent = calculatedAmount,
        )
    }
}