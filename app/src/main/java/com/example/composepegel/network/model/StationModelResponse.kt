package com.example.composepegel.network.model

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable

@ExperimentalSerializationApi
@Serializable
data class StationModelResponse(
    val uuid: String = "",
    val number: String = "",
    val shortname: String = "",
    val longname: String = "",
    val km: Double = 0.0,
    val agency: String = "",
    val water: WaterModelResponse? = null,
    val latitude: Double? = null,
    val longitude: Double? = null,
    val timeseries: List<TimeSeriesModelResponse> = listOf()
)