package com.rviewer.beers.adapters.repository

import com.rviewer.beers.adapters.repository.entity.DispenserEntity
import org.springframework.data.jpa.repository.JpaRepository

interface DispenserRepository : JpaRepository<DispenserEntity, String>