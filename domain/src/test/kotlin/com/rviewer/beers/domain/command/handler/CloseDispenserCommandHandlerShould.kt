package com.rviewer.beers.domain.command.handler

import com.rviewer.beers.domain.calcualor.SpendingCalculator
import com.rviewer.beers.domain.exception.DispenserNotOpenedException
import com.rviewer.beers.domain.model.Dispenser
import com.rviewer.beers.domain.model.DispenserStatus
import com.rviewer.beers.domain.model.Usage
import com.rviewer.beers.domain.mother.CloseDispenserCommandMother
import com.rviewer.beers.domain.mother.DispenserMother
import com.rviewer.beers.domain.mother.UsageMother
import com.rviewer.beers.domain.port.DispenserRepositoryPort
import com.rviewer.beers.domain.port.UsageRepositoryPort
import io.kotest.matchers.shouldBe
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.junit5.MockKExtension
import io.mockk.slot
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
    
    private val storedDispenser = DispenserMother.of(status = DispenserStatus.OPENED)
    private val dispenserSlot = slot<Dispenser>()
    private val usageSlot = slot<Usage>()
    
    @BeforeEach
    internal fun setUp() {
        every { dispenserRepository.findByIdRequired(any()) } returns storedDispenser
    }
    
    @AfterEach
    internal fun tearDown() {
        confirmVerified(
            dispenserRepository,
            usageRepository,
            clock,
            spendingCalculator,
        )
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
        val expectedClosedAt = Instant.now()
        val calculatedAmount = Random.nextDouble().toBigDecimal()
        every { usageRepository.findOpened(any()) } returns storedUsage
        every { clock.instant() } returns expectedClosedAt
        every { spendingCalculator.calculate(any(), any()) } returns calculatedAmount
        
        // when
        handler.handle(givenCommand)
        
        // then
        verify(exactly = 1) { dispenserRepository.findByIdRequired(givenCommand.dispenserId) }
        verify(exactly = 1) { usageRepository.findOpened(givenCommand.dispenserId) }
        verify(exactly = 1) { spendingCalculator.calculate(storedUsage, expectedClosedAt) }
        verify(exactly = 1) { clock.instant() }
        verifyUsageUpdated(storedUsage, expectedClosedAt, calculatedAmount)
        verify(exactly = 1) { dispenserRepository.update(capture(dispenserSlot)) }
        dispenserSlot.captured shouldBe storedDispenser.copy(
            status = DispenserStatus.CLOSED
        )
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