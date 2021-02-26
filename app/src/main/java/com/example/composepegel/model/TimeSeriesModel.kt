package com.example.composepegel.model

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable

@ExperimentalSerializationApi
@Serializable
data class TimeSeriesModel(
    val shortname: String = "",
    val longname: String = "",
    val unit: String = "",
    val equidistance: Int = 0,
    val currentMeasurement: CurrentMeasurementModel? = null,
    val gaugeZero: GaugeZeroModel? = null
)