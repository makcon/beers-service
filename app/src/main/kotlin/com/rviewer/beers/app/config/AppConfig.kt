package com.rviewer.beers.app.config

import com.rviewer.beers.domain.config.PriceConfig
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.math.BigDecimal
import java.time.Clock

@Configuration
class AppConfig {
    
    @Bean
    fun clock(): Clock = Clock.systemUTC()
    
    @Bean
    fun priceConfig(
        @Value("\${price.per.litre:\${PRICE_PER_LITRE:12.25}}") pricePerLitre: BigDecimal,
    ) = PriceConfig(
        pricePerLitre = pricePerLitre
    )
}