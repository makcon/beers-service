package com.rviewer.beers.domain.command.handler

import com.rviewer.beers.domain.command.OpenDispenserCommand
import com.rviewer.beers.domain.command.mother.DispenserMother
import com.rviewer.beers.domain.command.mother.OpenDispenserCommandMother
import com.rviewer.beers.domain.exception.DispenserInUseException
import com.rviewer.beers.domain.model.Dispenser
import com.rviewer.beers.domain.model.DispenserStatus
import com.rviewer.beers.domain.model.Usage
import com.rviewer.beers.domain.port.DispenserRepositoryPort
import com.rviewer.beers.domain.port.UsageRepositoryPort
import com.rviewer.beers.domain.utils.IdGenerator
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
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import java.time.Clock
import java.time.Instant
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
    
    @MockK
    lateinit var clock: Clock
    
    private val dispenserSlot = slot<Dispenser>()
    private val usageSlot = slot<Usage>()
    
    @AfterEach
    internal fun tearDown() {
        confirmVerified(
            dispenserRepository,
            usageRepository,
            idGenerator,
            clock,
        )
    }
    
    @Test
    fun `throw in use exception when dispenser is already opened`() {
        // given
        val storedDispenser = DispenserMother.of(status = DispenserStatus.OPENED)
        val givenCommand = OpenDispenserCommandMother.of()
        every { dispenserRepository.findByIdRequired(any()) } returns storedDispenser
        
        // when
        assertThrows<DispenserInUseException> { handler.handle(givenCommand) }
        
        // then
        verify(exactly = 1) { dispenserRepository.findByIdRequired(givenCommand.dispenserId) }
        verify(exactly = 0) { usageRepository.create(any()) }
        verify(exactly = 0) { dispenserRepository.update(any()) }
    }
    
    @Test
    fun `create usage and update status when dispenser is closed`() {
        // given
        val givenCommand = OpenDispenserCommandMother.of()
        val storedDispenser = DispenserMother.of(
            id = givenCommand.dispenserId,
            status = DispenserStatus.CLOSED
        )
        val generatedUsageId = UUID.randomUUID()
        every { dispenserRepository.findByIdRequired(any()) } returns storedDispenser
        every { idGenerator.usage() } returns generatedUsageId
        val expectedOpenedAt = Instant.now()
        every { clock.instant() } returns expectedOpenedAt
        
        // when
        handler.handle(givenCommand)
        
        // then
        verify(exactly = 1) { dispenserRepository.findByIdRequired(givenCommand.dispenserId) }
        verify(exactly = 1) { idGenerator.usage() }
        verify(exactly = 1) { clock.instant() }
        verifyUsageCreated(generatedUsageId, givenCommand, expectedOpenedAt, storedDispenser)
        verifyDispenserUpdated(storedDispenser)
    }
    
    private fun verifyUsageCreated(
        generatedUsageId: UUID,
        givenCommand: OpenDispenserCommand,
        expectedOpenedAt: Instant,
        storedDispenser: Dispenser,
    ) {
        verify(exactly = 1) { usageRepository.create(capture(usageSlot)) }
        usageSlot.captured shouldBe Usage(
            id = generatedUsageId,
            dispenserId = givenCommand.dispenserId,
            openedAt = expectedOpenedAt,
            closedAt = null,
            flowVolume = storedDispenser.flowVolume,
            totalSpent = null
        )
    }
    
    private fun verifyDispenserUpdated(storedDispenser: Dispenser) {
        verify(exactly = 1) { dispenserRepository.update(capture(dispenserSlot)) }
        dispenserSlot.captured shouldBe storedDispenser.copy(status = DispenserStatus.OPENED)
    }
}