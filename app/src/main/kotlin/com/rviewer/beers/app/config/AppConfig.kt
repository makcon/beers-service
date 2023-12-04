package com.rviewer.beers.app.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.time.Clock

@Configuration
class AppConfig {
    
    @Bean
    fun clock(): Clock = Clock.systemUTC()
}