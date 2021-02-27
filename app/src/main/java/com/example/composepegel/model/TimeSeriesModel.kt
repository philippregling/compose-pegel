package com.example.composepegel.model

data class TimeSeriesModel(
    val shortname: String = "",
    val longname: String = "",
    val unit: String = "",
    val equidistance: Int = 0,
    val currentMeasurement: CurrentMeasurementModel? = null,
    val gaugeZero: GaugeZeroModel? = null
)