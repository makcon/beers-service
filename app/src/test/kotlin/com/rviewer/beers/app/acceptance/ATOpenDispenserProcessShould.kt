package com.rviewer.beers.app.acceptance

import com.rviewer.beers.app.dto.ErrorCode
import com.rviewer.beers.app.dto.ErrorV1
import com.rviewer.beers.domain.model.DispenserStatus.CLOSED
import com.rviewer.beers.domain.model.DispenserStatus.OPENED
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import java.util.*

internal class ATOpenDispenserProcessShould : ATAbstractTest() {
    
    @Test
    fun `successfully open a dispenser`() {
        // given
        val storedDispenser = createDispenser(status = CLOSED)
        
        // when
        val actions = mvc.perform(buildOpenRequest(storedDispenser.id))
        
        // then
        actions.andExpect(MockMvcResultMatchers.status().isAccepted)
        verifyUsagesCreated(1)
    }
    
    @Test
    fun `return not found status when dispenser not found`() {
        // given
        val givenDispenserId = UUID.randomUUID()
        
        // when
        val actions = mvc.perform(buildOpenRequest(givenDispenserId))
        
        // then
        val content = actions.andExpect(MockMvcResultMatchers.status().isNotFound)
            .andReturn()
            .response
            .contentAsString
        val error = objectMapper.readValue(content, ErrorV1::class.java)
        error.code shouldBe ErrorCode.NOT_FOUND
        verifyUsagesCreated(0)
    }
    
    @Test
    fun `return conflict status when dispenser is already opened`() {
        // given
        val storedDispenser = createDispenser(status = OPENED)
        
        // when
        val actions = mvc.perform(buildOpenRequest(storedDispenser.id))
        
        // then
        val content = actions.andExpect(MockMvcResultMatchers.status().isConflict)
            .andReturn()
            .response
            .contentAsString
        val error = objectMapper.readValue(content, ErrorV1::class.java)
        error.code shouldBe ErrorCode.ALREADY_OPENED
        verifyUsagesCreated(0)
    }
    
    private fun buildOpenRequest(dispenserId: UUID): MockHttpServletRequestBuilder =
        MockMvcRequestBuilders.put("/v1/dispensers/$dispenserId/open")
    
    private fun verifyUsagesCreated(count: Int) {
        usageRepository.findAll().size shouldBe count
    }
}