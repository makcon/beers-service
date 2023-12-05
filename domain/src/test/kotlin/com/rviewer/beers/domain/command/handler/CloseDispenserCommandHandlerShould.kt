package com.rviewer.beers.domain.command.handler

import com.rviewer.beers.domain.calcualor.SpendingCalculator
import com.rviewer.beers.domain.exception.ClosedAtInvalidException
import com.rviewer.beers.domain.exception.DispenserInUseException
import com.rviewer.beers.domain.exception.DispenserNotOpenedException
import com.rviewer.beers.domain.model.Usage
import com.rviewer.beers.domain.mother.CloseDispenserCommandMother
import com.rviewer.beers.domain.mother.DispenserMother
import com.rviewer.beers.domain.mother.UsageMother
import com.rviewer.beers.domain.port.DispenserRepositoryPort
import com.rviewer.beers.domain.port.UsageRepositoryPort
import com.rviewer.beers.domain.validator.UsageValidator
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
    lateinit var spendingCalculator: SpendingCalculator
    
    @RelaxedMockK
    lateinit var usageValidator: UsageValidator
    
    private val storedDispenser = DispenserMother.of()
    private val usageSlot = slot<Usage>()
    private var givenCommand = CloseDispenserCommandMother.of(dispenserId = storedDispenser.id)
    
    @BeforeEach
    internal fun setUp() {
        every { dispenserRepository.findByIdRequired(any()) } returns storedDispenser
    }
    
    @AfterEach
    internal fun tearDown() {
        verify(exactly = 1) { dispenserRepository.findByIdRequired(givenCommand.dispenserId) }
    
        confirmVerified(
            dispenserRepository,
            usageRepository,
            spendingCalculator,
            usageValidator,
        )
    }
    
    @Test
    fun `does nothing when dispenser not found`() {
        // given
        givenCommand = CloseDispenserCommandMother.of()
        val expectedException = mockk<DispenserInUseException>()
        every { dispenserRepository.findByIdRequired(any()) } throws expectedException
    
        // when
        val exception = assertThrows<DispenserInUseException> { handler.handle(givenCommand) }
    
        // then
        exception shouldBe expectedException
    }
    
    @Test
    fun `throw not opened exception when opened usage not found`() {
        // given
        every { usageRepository.findOpened(any()) } returns null
    
        // when
        assertThrows<DispenserNotOpenedException> { handler.handle(givenCommand) }
    
        // then
        verify(exactly = 1) { usageRepository.findOpened(givenCommand.dispenserId) }
    }
    
    @Test
    fun `throw validation exception when usage validator throws`() {
        // given
        val expectedException = mockk<ClosedAtInvalidException>()
        every { usageValidator.validateClosedAt(any(), any()) } throws expectedException
        
        // when
        val exception = assertThrows<ClosedAtInvalidException> { handler.handle(givenCommand) }
        
        // then
        exception shouldBe expectedException
        verify(exactly = 1) { usageRepository.findOpened(givenCommand.dispenserId) }
        verify { usageValidator.validateClosedAt(capture(usageSlot), givenCommand.closedAt) }
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
        verify(exactly = 1) { usageRepository.findOpened(givenCommand.dispenserId) }
        verify { usageValidator.validateClosedAt(capture(usageSlot), givenCommand.closedAt) }
        verify(exactly = 1) { spendingCalculator.calculate(storedUsage, givenCommand.closedAt) }
        verify(exactly = 1) { usageRepository.update(capture(usageSlot)) }
        usageSlot.captured shouldBe storedUsage.copy(
            closedAt = givenCommand.closedAt,
            totalSpent = calculatedAmount,
        )
    }
}