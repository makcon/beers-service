package com.rviewer.beers.domain.validator

import com.rviewer.beers.domain.exception.ModelNotFoundException
import com.rviewer.beers.domain.port.DispenserRepositoryPort
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import io.mockk.verify
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import java.util.*

@ExtendWith(MockKExtension::class)
internal class DispenserValidatorShould {
    
    @InjectMockKs
    lateinit var validator: DispenserValidator
    
    @MockK
    lateinit var repository: DispenserRepositoryPort
    
    private val givenId = UUID.randomUUID()
    
    @AfterEach
    internal fun tearDown() {
        verify(exactly = 1) { repository.existsById(givenId) }
        confirmVerified(repository)
    }
    
    @Test
    fun `pass when repository return true`() {
        // given
        every { repository.existsById(any()) } returns true
        
        // when - then
        assertDoesNotThrow { validator.validateIfExists(givenId) }
    }
    
    @Test
    fun `throw exception when repository return false`() {
        // given
        every { repository.existsById(any()) } returns false
        
        // when - then
        assertThrows<ModelNotFoundException> { validator.validateIfExists(givenId) }
    }
}