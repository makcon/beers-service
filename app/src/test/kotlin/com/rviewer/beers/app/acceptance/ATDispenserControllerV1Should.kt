package com.rviewer.beers.app.acceptance

import com.fasterxml.jackson.databind.ObjectMapper
import com.rviewer.beers.app.dto.CreateDispenserRequestParamsV1
import com.rviewer.beers.app.dto.DispenserV1
import com.rviewer.beers.app.mother.CreateDispenserRequestParamsV1Mother
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers

@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
internal class ATDispenserControllerV1Should {
    
    @Autowired
    lateinit var objectMapper: ObjectMapper
    
    @Autowired
    lateinit var mvc: MockMvc
    
    @Test
    fun `create a dispenser`() {
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
    
    private fun buildCreateRequest(params: CreateDispenserRequestParamsV1): MockHttpServletRequestBuilder {
        return MockMvcRequestBuilders.post("/v1/dispensers")
            .content(objectMapper.writeValueAsString(params))
            .contentType(MediaType.APPLICATION_JSON)
    }
}