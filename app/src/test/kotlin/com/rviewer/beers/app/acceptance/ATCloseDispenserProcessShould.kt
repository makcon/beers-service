package com.rviewer.beers.app.acceptance

import com.rviewer.beers.app.dto.ErrorCode
import com.rviewer.beers.app.dto.ErrorV1
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import java.util.*

internal class ATCloseDispenserProcessShould : ATAbstractDispenserTest() {
    
    @Test
    fun `successfully close a dispenser`() {
        // given
        val storedDispenser = createDispenser()
        createUsage(dispenserId = storedDispenser.id, closedAt = null)
        
        // when
        val actions = mvc.perform(buildCloseRequest(storedDispenser.id))
        
        // then
        actions.andExpect(MockMvcResultMatchers.status().isAccepted)
        verifyUsagesUpdated(1)
    }
    
    @Test
    fun `return not found status when dispenser not found`() {
        // given
        val givenDispenserId = UUID.randomUUID()
        
        // when
        val actions = mvc.perform(buildCloseRequest(givenDispenserId))
        
        // then
        val content = actions
            .andExpect(MockMvcResultMatchers.status().isNotFound)
            .andReturn()
            .response
            .contentAsString
        val error = objectMapper.readValue(content, ErrorV1::class.java)
        error.code shouldBe ErrorCode.NOT_FOUND
        verifyUsagesUpdated(0)
    }
    
    @Test
    fun `return conflict status when opened usage not found`() {
        // given
        val storedDispenser = createDispenser()
        
        // when
        val actions = mvc.perform(buildCloseRequest(storedDispenser.id))
        
        // then
        val content = actions
            .andExpect(MockMvcResultMatchers.status().isConflict)
            .andReturn()
            .response
            .contentAsString
        val error = objectMapper.readValue(content, ErrorV1::class.java)
        error.code shouldBe ErrorCode.ALREADY_CLOSED
        verifyUsagesUpdated(0)
    }
    
    private fun buildCloseRequest(dispenserId: UUID): MockHttpServletRequestBuilder =
        MockMvcRequestBuilders.put("/v1/dispensers/$dispenserId/close")
    
    private fun verifyUsagesUpdated(count: Int) {
        usageRepository.findAll().filter { it.closedAt != null }.size shouldBe count
    }
}