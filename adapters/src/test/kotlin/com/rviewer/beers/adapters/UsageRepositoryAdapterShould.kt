package com.rviewer.beers.adapters

import com.rviewer.beers.adapters.mother.UsageMother
import com.rviewer.beers.adapters.repository.UsageRepository
import com.rviewer.beers.adapters.repository.mapper.toEntity
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
    fun create() {
        // given
        val givenUsage = UsageMother.of()
        
        // when
        adapter.create(givenUsage)
        
        // then
        verify(exactly = 1) { repository.save(givenUsage.toEntity()) }
    }
}