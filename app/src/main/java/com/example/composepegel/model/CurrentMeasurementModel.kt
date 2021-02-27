package com.example.composepegel.model

import org.threeten.bp.ZonedDateTime


data class CurrentMeasurementModel(
    val timeStamp: ZonedDateTime = ZonedDateTime.now(),
    val value: Double = 0.0,
    val trend: Int = 0,
    val stateMnwMhw: String = "",
    val stateNswHsw: String = ""
)