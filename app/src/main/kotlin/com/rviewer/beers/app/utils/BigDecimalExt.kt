package com.rviewer.beers.app.utils

import java.math.BigDecimal
import java.math.RoundingMode

fun BigDecimal.withPrecision(
    scale: Int = 2,
    roundingMode: RoundingMode = RoundingMode.HALF_UP,
): BigDecimal = this.setScale(scale, roundingMode)