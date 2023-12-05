package com.rviewer.beers.app.acceptance

import com.rviewer.beers.app.dto.CreateDispenserRequestParamsV1
import com.rviewer.beers.app.dto.DispenserV1
import com.rviewer.beers.app.mother.CreateDispenserRequestParamsV1Mother
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers

internal class ATCreateDispenserProcessShould : ATAbstractDispenserTest() {
    
    @Test
    fun `successfully create a dispenser`() {
        // given
        val givenParams = CreateDispenserRequestParamsV1Mother.of()
        
        // when
        val actions = mvc.perform(buildCreateRequest(givenParams))
        
        // then
        val content = actions
            .andExpect(MockMvcResultMatchers.status().isCreated)
            .andReturn()
            .response
            .contentAsString
        val dispenser = objectMapper.readValue(content, DispenserV1::class.java)
        dispenser.flowVolume shouldBe givenParams.flowVolume
    }
    
    private fun buildCreateRequest(params: CreateDispenserRequestParamsV1): MockHttpServletRequestBuilder =
        MockMvcRequestBuilders
            .post("/v1/dispensers")
            .content(objectMapper.writeValueAsString(params))
            .contentType(MediaType.APPLICATION_JSON)
}