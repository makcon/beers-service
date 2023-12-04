package com.rviewer.beers.adapters

import com.rviewer.beers.adapters.mother.DispenserEntityMother
import com.rviewer.beers.adapters.mother.DispenserMother
import com.rviewer.beers.adapters.repository.DispenserRepository
import com.rviewer.beers.adapters.repository.mapper.toEntity
import com.rviewer.beers.adapters.repository.mapper.toModel
import com.rviewer.beers.domain.exception.ModelNotFoundException
import io.kotest.matchers.shouldBe
import io.mockk.*
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import java.util.*

@ExtendWith(MockKExtension::class)
internal class DispenserRepositoryAdapterShould {
    
    @InjectMockKs
    lateinit var adapter: DispenserRepositoryAdapter
    
    @MockK
    lateinit var repository: DispenserRepository
    
    @BeforeEach
    internal fun setUp() {
        every { repository.save(any()) } returns mockk()
    }
    
    @AfterEach
    internal fun tearDown() {
        confirmVerified(repository)
    }
    
    @Test
    fun `call repository to create dispenser`() {
        // given
        val givenDispenser = DispenserMother.of()
        
        // when
        adapter.create(givenDispenser)
        
        // then
        verify { repository.save(givenDispenser.toEntity()) }
    }
    
    @Test
    fun `call repository to update dispenser`() {
        // given
        val givenDispenser = DispenserMother.of()
        
        // when
        adapter.update(givenDispenser)
        
        // then
        verify { repository.save(givenDispenser.toEntity()) }
    }
    
    @Test
    fun `find entity by id and map to model`() {
        // given
        val givenId = UUID.randomUUID()
        val storedEntity = DispenserEntityMother.of()
        every { repository.findById(any()) } returns Optional.of(storedEntity)
        
        // when
        val dispenser = adapter.findById(givenId)
        
        // then
        dispenser shouldBe storedEntity.toModel()
        verify { repository.findById(givenId.toString()) }
    }
    
    @Test
    fun `return null when entity not found`() {
        // given
        val givenId = UUID.randomUUID()
        every { repository.findById(any()) } returns Optional.empty()
        
        // when
        val dispenser = adapter.findById(givenId)
        
        // then
        dispenser shouldBe null
        verify { repository.findById(givenId.toString()) }
    }
    
    @Test
    fun `throw exception when required entity not found`() {
        // given
        val givenId = UUID.randomUUID()
        every { repository.findById(any()) } returns Optional.empty()
        
        // when
        assertThrows<ModelNotFoundException> { adapter.findByIdRequired(givenId) }
        
        // then
        verify { repository.findById(givenId.toString()) }
    }
}