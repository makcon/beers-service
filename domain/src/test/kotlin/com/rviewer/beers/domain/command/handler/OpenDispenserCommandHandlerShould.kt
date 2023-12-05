package com.rviewer.beers.domain.command.handler

import com.rviewer.beers.domain.command.OpenDispenserCommand
import com.rviewer.beers.domain.exception.DispenserInUseException
import com.rviewer.beers.domain.model.Dispenser
import com.rviewer.beers.domain.model.Usage
import com.rviewer.beers.domain.mother.DispenserMother
import com.rviewer.beers.domain.mother.OpenDispenserCommandMother
import com.rviewer.beers.domain.port.DispenserRepositoryPort
import com.rviewer.beers.domain.port.UsageRepositoryPort
import com.rviewer.beers.domain.utils.IdGenerator
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
import java.util.*

@ExtendWith(MockKExtension::class)
internal class OpenDispenserCommandHandlerShould {
    
    @InjectMockKs
    lateinit var handler: OpenDispenserCommandHandler
    
    @RelaxedMockK
    lateinit var dispenserRepository: DispenserRepositoryPort
    
    @RelaxedMockK
    lateinit var usageRepository: UsageRepositoryPort
    
    @MockK
    lateinit var idGenerator: IdGenerator
    
    private val usageSlot = slot<Usage>()
    private val generatedUsageId = UUID.randomUUID()
    
    @BeforeEach
    internal fun setUp() {
        every { idGenerator.usage() } returns generatedUsageId
    }
    
    @AfterEach
    internal fun tearDown() {
        confirmVerified(
            dispenserRepository,
            usageRepository,
            idGenerator,
        )
    }
    
    @Test
    fun `does nothing when dispenser not found`() {
        // given
        val givenCommand = OpenDispenserCommandMother.of()
        every { dispenserRepository.findByIdRequired(any()) } throws mockk()
        
        // when
        assertThrows<DispenserInUseException> { handler.handle(givenCommand) }
        
        // then
        verify(exactly = 1) { dispenserRepository.findByIdRequired(givenCommand.dispenserId) }
    }
    
    @Test
    fun `throw in use exception when dispenser is already opened`() {
        // given
        val storedDispenser = DispenserMother.of()
        val givenCommand = OpenDispenserCommandMother.of()
        every { dispenserRepository.findByIdRequired(any()) } returns storedDispenser
        every { usageRepository.findOpened(any()) } returns mockk()
        
        // when
        assertThrows<DispenserInUseException> { handler.handle(givenCommand) }
        
        // then
        verify(exactly = 1) { dispenserRepository.findByIdRequired(givenCommand.dispenserId) }
        verify(exactly = 1) { usageRepository.findOpened(givenCommand.dispenserId) }
        verify(exactly = 0) { usageRepository.create(any()) }
    }
    
    @Test
    fun `create usage when dispenser is closed`() {
        // given
        val givenCommand = OpenDispenserCommandMother.of()
        val storedDispenser = DispenserMother.of(
            id = givenCommand.dispenserId,
        )
        every { dispenserRepository.findByIdRequired(any()) } returns storedDispenser
        every { usageRepository.findOpened(any()) } returns null
        
        // when
        handler.handle(givenCommand)
        
        // then
        verify(exactly = 1) { dispenserRepository.findByIdRequired(givenCommand.dispenserId) }
        verify(exactly = 1) { usageRepository.findOpened(givenCommand.dispenserId) }
        verify(exactly = 1) { idGenerator.usage() }
        verifyUsageCreated(givenCommand, storedDispenser)
    }
    
    private fun verifyUsageCreated(
        command: OpenDispenserCommand,
        storedDispenser: Dispenser,
    ) {
        verify(exactly = 1) { usageRepository.create(capture(usageSlot)) }
        usageSlot.captured shouldBe Usage(
            id = generatedUsageId,
            dispenserId = storedDispenser.id,
            openedAt = command.openedAt,
            closedAt = null,
            flowVolume = storedDispenser.flowVolume,
            totalSpent = null
        )
    }
}