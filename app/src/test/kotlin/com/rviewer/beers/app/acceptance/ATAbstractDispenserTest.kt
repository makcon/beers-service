package com.rviewer.beers.app.acceptance

import com.fasterxml.jackson.databind.ObjectMapper
import com.rviewer.beers.adapters.repository.UsageRepository
import com.rviewer.beers.app.mother.DispenserMother
import com.rviewer.beers.app.mother.UsageMother
import com.rviewer.beers.domain.model.Dispenser
import com.rviewer.beers.domain.model.Usage
import com.rviewer.beers.domain.port.DispenserRepositoryPort
import com.rviewer.beers.domain.port.UsageRepositoryPort
import org.junit.jupiter.api.AfterEach
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment
import org.springframework.test.web.servlet.MockMvc
import java.time.Instant
import java.util.*

@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
abstract class ATAbstractDispenserTest {
    
    @Autowired
    lateinit var mvc: MockMvc
    
    @Autowired
    lateinit var objectMapper: ObjectMapper
    
    @Autowired
    lateinit var dispenserRepositoryPort: DispenserRepositoryPort
    
    @Autowired
    lateinit var usageRepository: UsageRepository
    
    @Autowired
    lateinit var usageRepositoryPort: UsageRepositoryPort
    
    @AfterEach
    internal fun tearDown() {
        usageRepository.deleteAll()
    }
    
    fun createDispenser(): Dispenser {
        val dispenser = DispenserMother.of()
        dispenserRepositoryPort.create(dispenser)
        
        return dispenser
    }
    
    fun createUsage(dispenserId: UUID, closedAt: Instant? = Instant.now()): Usage {
        val usage = UsageMother.of(
            dispenserId = dispenserId,
            closedAt = closedAt
        )
        usageRepositoryPort.create(usage)
        
        return usage
    }
}