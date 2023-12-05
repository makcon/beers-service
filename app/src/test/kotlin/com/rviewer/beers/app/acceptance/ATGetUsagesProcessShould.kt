package com.rviewer.beers.app.acceptance

import com.rviewer.beers.app.dto.GetSpendingResponseV1
import com.rviewer.beers.app.mapper.toDto
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test
import org.springframework.test.web.servlet.ResultActions
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import java.util.*

internal class ATGetUsagesProcessShould : ATAbstractDispenserTest() {
    
    @Test
    fun `returned sorted usages response`() {
        // given
        val storedDispenser = createDispenser()
        val storedUsage = createUsage(storedDispenser.id).toDto()
        val storedUsage2 = createUsage(storedDispenser.id).toDto()
        createUsage(UUID.randomUUID())
        
        // when
        val actions = mvc.perform(buildGetRequest(storedDispenser.id))
        
        // then
        val response = verifyAndGetResponse(actions)
        response shouldBe GetSpendingResponseV1(
            totalCount = 2,
            totalAmount = listOf(storedUsage2, storedUsage).sumOf { it.totalSpent!! },
            usages = listOf(storedUsage2, storedUsage)
        )
    }
    
    @Test
    fun `return first page of sorted usages`() {
        // given
        val storedDispenser = createDispenser()
        val storedUsage = createUsage(storedDispenser.id).toDto()
        val storedUsage2 = createUsage(storedDispenser.id).toDto()
        val storedUsage3 = createUsage(storedDispenser.id).toDto()
        
        // when
        val actions = mvc.perform(buildGetRequest(storedDispenser.id))
        
        // then
        val response = verifyAndGetResponse(actions)
        response shouldBe GetSpendingResponseV1(
            totalCount = 3,
            totalAmount = listOf(storedUsage, storedUsage3, storedUsage2).sumOf { it.totalSpent!! },
            usages = listOf(storedUsage3, storedUsage2)
        )
    }
    
    @Test
    fun `returned result when page info not provided`() {
        // given
        val storedDispenser = createDispenser()
        val storedUsage = createUsage(storedDispenser.id).toDto()
        
        // when
        val actions = mvc.perform(get("/v1/dispensers/${storedDispenser.id}/spending"))
        
        // then
        val response = verifyAndGetResponse(actions)
        response shouldBe GetSpendingResponseV1(
            totalCount = 1,
            totalAmount = storedUsage.totalSpent!!,
            usages = listOf(storedUsage)
        )
    }
    
    private fun verifyAndGetResponse(actions: ResultActions): GetSpendingResponseV1? {
        val content = actions
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andReturn()
            .response
            .contentAsString
        
        return objectMapper.readValue(content, GetSpendingResponseV1::class.java)
    }
    
    private fun buildGetRequest(
        dispenserId: UUID,
        pageNumber: Int = 0,
        pageSize: Int = 2,
    ): MockHttpServletRequestBuilder =
        get("/v1/dispensers/$dispenserId/spending?pageNumber=$pageNumber&pageSize=$pageSize")
}