package com.rviewer.beers.adapters.config

import org.springframework.boot.autoconfigure.domain.EntityScan
import org.springframework.context.annotation.Configuration
import org.springframework.data.jpa.repository.config.EnableJpaRepositories

@Configuration
@EnableJpaRepositories(basePackages = ["com.rviewer.beers.adapters.repository"])
@EntityScan(basePackages = ["com.rviewer.beers.adapters.repository.entity"])
class ReposConfig