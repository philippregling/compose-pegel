package com.example.composepegel.model

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable

@ExperimentalSerializationApi
@Serializable
data class StationModel(
    val uuid: String = "",
    val number: String = "",
    val shortname: String = "",
    val longname: String = "",
    val km: Double = 0.0,
    val agency: String = "",
    val water: WaterModel? = null,
    val timeseries: List<TimeSeriesModel> = listOf()
)