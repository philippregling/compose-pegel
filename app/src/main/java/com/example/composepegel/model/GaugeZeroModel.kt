package com.example.composepegel.model

import org.threeten.bp.LocalDate

data class GaugeZeroModel(
    val unit: String = "",
    val value: Double = 0.0,
    val validFrom: LocalDate = LocalDate.now()
)
