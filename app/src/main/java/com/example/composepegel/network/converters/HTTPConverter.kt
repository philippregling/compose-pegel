package com.example.composepegel.network.converters

import com.example.composepegel.model.CurrentMeasurementModel
import com.example.composepegel.model.GaugeZeroModel
import com.example.composepegel.model.StationModel
import com.example.composepegel.model.TimeSeriesModel
import com.example.composepegel.model.WaterModel
import com.example.composepegel.network.model.CurrentMeasurementModelResponse
import com.example.composepegel.network.model.GaugeZeroModelResponse
import com.example.composepegel.network.model.StationModelResponse
import com.example.composepegel.network.model.TimeSeriesModelResponse
import com.example.composepegel.network.model.WaterModelResponse
import kotlinx.serialization.ExperimentalSerializationApi

@ExperimentalSerializationApi
fun List<StationModelResponse>.convertStations(): List<StationModel> {
    return map { it.convert() }
}

@ExperimentalSerializationApi
fun StationModelResponse.convert(): StationModel {
    return StationModel(
        uuid,
        number,
        shortname,
        longname,
        km,
        agency,
        water?.convert(),
        latitude ?: 0.0,
        longitude ?: 0.0,
        timeseries.convertTimeSeries()
    )
}

fun List<WaterModelResponse>.convertWaters(): List<WaterModel> {
    return map { it.convert() }
}

fun WaterModelResponse.convert(): WaterModel {
    return WaterModel(
        shortname, longname
    )
}

@ExperimentalSerializationApi
fun List<TimeSeriesModelResponse>.convertTimeSeries(): List<TimeSeriesModel> {
    return map { it.convert() }
}

@ExperimentalSerializationApi
fun TimeSeriesModelResponse.convert(): TimeSeriesModel {
    return TimeSeriesModel(
        shortname, longname, unit, equidistance, currentMeasurement?.convert(), gaugeZero?.convert()
    )
}

@ExperimentalSerializationApi
fun CurrentMeasurementModelResponse.convert(): CurrentMeasurementModel {
    return CurrentMeasurementModel(
        timeStamp, value, trend, stateMnwMhw, stateNswHsw
    )
}

@ExperimentalSerializationApi
fun GaugeZeroModelResponse.convert(): GaugeZeroModel {
    return GaugeZeroModel(
        unit, value, validFrom
    )
}