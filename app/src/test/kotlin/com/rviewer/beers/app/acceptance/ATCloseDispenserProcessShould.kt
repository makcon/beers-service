package com.rviewer.beers.app.acceptance

import com.rviewer.beers.app.dto.CloseDispenserRequestParamsV1
import com.rviewer.beers.app.dto.ErrorCode
import com.rviewer.beers.app.mother.CloseDispenserRequestParamsV1Mother
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
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
        val givenParams = CloseDispenserRequestParamsV1Mother.of()
        
        // when
        val actions = mvc.perform(buildCloseRequest(storedDispenser.id, givenParams))
        
        // then
        actions.andExpect(MockMvcResultMatchers.status().isAccepted)
        verifyUsagesUpdated(1)
    }
    
    @Test
    fun `return not found status when dispenser not found`() {
        // given
        val givenDispenserId = UUID.randomUUID()
        val givenParams = CloseDispenserRequestParamsV1Mother.of()
        
        // when
        val actions = mvc.perform(buildCloseRequest(givenDispenserId, givenParams))
        
        // then
        val error = verifyAndGetError(actions, HttpStatus.NOT_FOUND)
        error.code shouldBe ErrorCode.NOT_FOUND
        verifyUsagesUpdated(0)
    }
    
    @Test
    fun `return conflict status when opened usage not found`() {
        // given
        val storedDispenser = createDispenser()
        val givenParams = CloseDispenserRequestParamsV1Mother.of()
    
        // when
        val actions = mvc.perform(buildCloseRequest(storedDispenser.id, givenParams))
    
        // then
        val error = verifyAndGetError(actions, HttpStatus.CONFLICT)
        error.code shouldBe ErrorCode.ALREADY_CLOSED
        verifyUsagesUpdated(0)
    }
    
    @Test
    fun `return bad request status when closed at is not valid`() {
        // given
        val storedDispenser = createDispenser()
        val storedUsage = createUsage(dispenserId = storedDispenser.id, closedAt = null)
        val givenParams = CloseDispenserRequestParamsV1Mother.of(
            closedAt = storedUsage.openedAt
        )
        
        // when
        val actions = mvc.perform(buildCloseRequest(storedDispenser.id, givenParams))
        
        // then
        val error = verifyAndGetError(actions, HttpStatus.BAD_REQUEST)
        error.code shouldBe ErrorCode.CLOSED_AT_INVALID
        verifyUsagesUpdated(0)
    }
    
    private fun buildCloseRequest(
        dispenserId: UUID,
        params: CloseDispenserRequestParamsV1,
    ): MockHttpServletRequestBuilder = MockMvcRequestBuilders
        .put("/v1/dispensers/$dispenserId/close")
        .content(objectMapper.writeValueAsString(params))
        .contentType(MediaType.APPLICATION_JSON)
    
    private fun verifyUsagesUpdated(count: Int) {
        usageRepository.findAll().filter { it.closedAt != null }.size shouldBe count
    }
}