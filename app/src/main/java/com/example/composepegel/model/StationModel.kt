package com.example.composepegel.model

data class StationModel(
    val uuid: String = "",
    val number: String = "",
    val shortname: String = "",
    val longname: String = "",
    val km: Double = 0.0,
    val agency: String = "",
    val water: WaterModel? = null,
    val latitude: Double = 0.0,
    val longitude: Double = 0.0,
    val timeseries: List<TimeSeriesModel> = listOf()
)