package com.example.composepegel.network.model

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable

@ExperimentalSerializationApi
@Serializable
data class TimeSeriesModelResponse(
    val shortname: String = "",
    val longname: String = "",
    val unit: String = "",
    val equidistance: Int = 0,
    val currentMeasurement: CurrentMeasurementModelResponse? = null,
    val gaugeZero: GaugeZeroModelResponse? = null
)