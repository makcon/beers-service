package com.rviewer.beers.adapters

import com.rviewer.beers.adapters.mother.UsageEntityMother
import com.rviewer.beers.adapters.mother.UsageMother
import com.rviewer.beers.adapters.repository.UsageRepository
import com.rviewer.beers.adapters.repository.mapper.toEntity
import com.rviewer.beers.adapters.repository.mapper.toModel
import io.kotest.matchers.shouldBe
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.junit5.MockKExtension
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mock
import java.util.*

@ExtendWith(MockKExtension::class)
internal class UsageRepositoryAdapterShould {
    
    @InjectMockKs
    lateinit var adapter: UsageRepositoryAdapter
    
    @Mock
    lateinit var repository: UsageRepository
    
    @BeforeEach
    internal fun setUp() {
        every { repository.save(any()) } returns mockk()
    }
    
    @AfterEach
    internal fun tearDown() {
        confirmVerified(repository)
    }
    
    @Test
    fun `call repository to create usage`() {
        // given
        val givenUsage = UsageMother.of()
        
        // when
        adapter.create(givenUsage)
        
        // then
        verify(exactly = 1) { repository.save(givenUsage.toEntity()) }
    }
    
    @Test
    fun `call repository to update usage`() {
        // given
        val givenUsage = UsageMother.of()
        
        // when
        adapter.update(givenUsage)
        
        // then
        verify(exactly = 1) { repository.save(givenUsage.toEntity()) }
    }
    
    @Test
    fun `find opened usage by dispenser id and map to model`() {
        // given
        val givenDispenserId = UUID.randomUUID()
        val storedEntity = UsageEntityMother.of()
        every { repository.findByDispenserIdAndClosedAtNull(any()) } returns storedEntity
        
        // when
        val usage = adapter.findOpened(givenDispenserId)
        
        // then
        usage shouldBe storedEntity.toModel()
        verify { repository.findByDispenserIdAndClosedAtNull(givenDispenserId.toString()) }
    }
    
    @Test
    fun `return null when opened usage not found by dispenser id`() {
        // given
        val givenDispenserId = UUID.randomUUID()
        every { repository.findByDispenserIdAndClosedAtNull(any()) } returns null
        
        // when
        val usage = adapter.findOpened(givenDispenserId)
        
        // then
        usage shouldBe null
        verify { repository.findByDispenserIdAndClosedAtNull(givenDispenserId.toString()) }
    }
}