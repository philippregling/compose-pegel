package com.example.composepegel.util

fun generateMeasurementsUrl(stationUuid: String, measurementType: String): String {
    return "https://www.pegelonline.wsv.de/webservices/rest-api/v2/stations/$stationUuid/$measurementType/measurements.png?start=P20D"
}