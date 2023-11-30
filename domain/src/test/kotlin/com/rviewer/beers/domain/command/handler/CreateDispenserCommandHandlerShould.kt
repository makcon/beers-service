package com.rviewer.beers.domain.command.handler

import com.rviewer.beers.domain.command.mother.CreateDispenserCommandMother
import com.rviewer.beers.domain.model.Dispenser
import com.rviewer.beers.domain.model.DispenserStatus
import com.rviewer.beers.domain.port.DispenserRepositoryPort
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
import org.junit.jupiter.api.extension.ExtendWith
import java.util.*

@ExtendWith(MockKExtension::class)
internal class CreateDispenserCommandHandlerShould {
    
    @InjectMockKs
    lateinit var handler: CreateDispenserCommandHandler
    
    @RelaxedMockK
    lateinit var repository: DispenserRepositoryPort
    
    @MockK
    lateinit var idGenerator: IdGenerator
    
    private val dispenserSlot = slot<Dispenser>()
    
    @AfterEach
    internal fun tearDown() {
        confirmVerified(repository)
    }
    
    @Test
    fun handle() {
        // given
        val givenCommand = CreateDispenserCommandMother.of()
        val generatedDispenserId = UUID.randomUUID()
        every { idGenerator.dispenser() } returns generatedDispenserId
        val expectedDispenser = Dispenser(
            id = generatedDispenserId,
            flowVolume = givenCommand.flowVolume,
            status = DispenserStatus.CLOSED,
        )
        
        // when
        val dispenser = handler.handle(givenCommand)
        
        // then
        dispenser shouldBe expectedDispenser
        verify(exactly = 1) { repository.create(capture(dispenserSlot)) }
        dispenserSlot.captured shouldBe expectedDispenser
    }
}