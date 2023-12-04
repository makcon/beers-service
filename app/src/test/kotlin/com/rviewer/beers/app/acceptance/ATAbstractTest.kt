package com.rviewer.beers.app.acceptance

import com.fasterxml.jackson.databind.ObjectMapper
import com.rviewer.beers.adapters.repository.UsageRepository
import com.rviewer.beers.app.mother.DispenserMother
import com.rviewer.beers.domain.model.Dispenser
import com.rviewer.beers.domain.model.DispenserStatus
import com.rviewer.beers.domain.port.DispenserRepositoryPort
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment
import org.springframework.test.web.servlet.MockMvc

@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
abstract class ATAbstractTest {
    
    @Autowired
    lateinit var mvc: MockMvc
    
    @Autowired
    lateinit var objectMapper: ObjectMapper
    
    @Autowired
    lateinit var dispenserRepositoryPort: DispenserRepositoryPort
    
    @Autowired
    lateinit var usageRepository: UsageRepository
    
    fun createDispenser(status: DispenserStatus): Dispenser {
        val dispenser = DispenserMother.of(status = status)
        dispenserRepositoryPort.create(dispenser)
        
        return dispenser
    }
}