package com.rviewer.beers.domain.utils

import org.springframework.stereotype.Component
import java.util.*

@Component
class IdGenerator {
    
    fun dispenser(): UUID = UUID.randomUUID()
    
    fun usage(): UUID = UUID.randomUUID()
}