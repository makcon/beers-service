package com.rviewer.beers.adapters.repository.entity

import com.rviewer.beers.domain.model.DispenserStatus
import jakarta.persistence.Entity
import jakarta.persistence.Id

@Entity(name = "dispensers")
data class DispenserEntity(
    @field:Id
    val id: String,
    val flowVolume: Float,
    val status: DispenserStatus,
)