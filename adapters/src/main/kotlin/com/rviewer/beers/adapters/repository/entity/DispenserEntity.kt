package com.rviewer.beers.adapters.repository.entity

import jakarta.persistence.Entity
import jakarta.persistence.Id

@Entity(name = "dispensers")
data class DispenserEntity(
    @field:Id
    val id: String,
    val flowVolume: Float,
)