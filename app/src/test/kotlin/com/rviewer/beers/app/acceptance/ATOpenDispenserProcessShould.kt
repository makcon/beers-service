package com.rviewer.beers.app.acceptance

import com.rviewer.beers.app.dto.ErrorCode
import com.rviewer.beers.app.dto.OpenDispenserRequestParamsV1
import com.rviewer.beers.app.mother.OpenDispenserRequestParamsV1Mother
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import java.util.*

internal class ATOpenDispenserProcessShould : ATAbstractDispenserTest() {
    
    @Test
    fun `successfully open a dispenser`() {
        // given
        val storedDispenser = createDispenser()
        val givenParams = OpenDispenserRequestParamsV1Mother.of()
        
        // when
        val actions = mvc.perform(buildOpenRequest(storedDispenser.id, givenParams))
        
        // then
        actions.andExpect(MockMvcResultMatchers.status().isAccepted)
        verifyUsagesCreated(1)
    }
    
    @Test
    fun `return not found status when dispenser not found`() {
        // given
        val givenDispenserId = UUID.randomUUID()
        val givenParams = OpenDispenserRequestParamsV1Mother.of()
        
        // when
        val actions = mvc.perform(buildOpenRequest(givenDispenserId, givenParams))
        
        // then
        val error = verifyAndGetError(actions, HttpStatus.NOT_FOUND)
        error.code shouldBe ErrorCode.NOT_FOUND
        verifyUsagesCreated(0)
    }
    
    @Test
    fun `return conflict status when dispenser is already opened`() {
        // given
        val storedDispenser = createDispenser()
        createUsage(dispenserId = storedDispenser.id, closedAt = null)
        val givenParams = OpenDispenserRequestParamsV1Mother.of()
    
        // when
        val actions = mvc.perform(buildOpenRequest(storedDispenser.id, givenParams))
    
        // then
        val error = verifyAndGetError(actions, HttpStatus.CONFLICT)
        error.code shouldBe ErrorCode.ALREADY_OPENED
        verifyUsagesCreated(1)
    }
    
    private fun buildOpenRequest(
        dispenserId: UUID,
        params: OpenDispenserRequestParamsV1,
    ): MockHttpServletRequestBuilder = MockMvcRequestBuilders
        .put("/v1/dispensers/$dispenserId/open")
        .content(objectMapper.writeValueAsString(params))
        .contentType(MediaType.APPLICATION_JSON)
    
    private fun verifyUsagesCreated(count: Int) {
        usageRepository.findAll().size shouldBe count
    }
}