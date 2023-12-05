package com.rviewer.beers.app.acceptance

import com.rviewer.beers.app.dto.ErrorCode
import com.rviewer.beers.app.dto.GetSpendingResponseV1
import com.rviewer.beers.app.mapper.toDto
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test
import org.springframework.http.HttpStatus
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
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
        val response = verifyAndGetObject(actions, HttpStatus.OK, GetSpendingResponseV1::class.java)
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
        val response = verifyAndGetObject(actions, HttpStatus.OK, GetSpendingResponseV1::class.java)
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
        val response = verifyAndGetObject(actions, HttpStatus.OK, GetSpendingResponseV1::class.java)
        response shouldBe GetSpendingResponseV1(
            totalCount = 1,
            totalAmount = storedUsage.totalSpent!!,
            usages = listOf(storedUsage)
        )
    }
    
    @Test
    fun `return not found status when dispenser not found`() {
        // given
        val givenDispenserId = UUID.randomUUID()
        
        // when
        val actions = mvc.perform(buildGetRequest(givenDispenserId))
        
        // then
        val error = verifyAndGetError(actions, HttpStatus.NOT_FOUND)
        error.code shouldBe ErrorCode.NOT_FOUND
    }
    
    private fun buildGetRequest(
        dispenserId: UUID,
        pageNumber: Int = 0,
        pageSize: Int = 2,
    ): MockHttpServletRequestBuilder =
        get("/v1/dispensers/$dispenserId/spending?pageNumber=$pageNumber&pageSize=$pageSize")
}