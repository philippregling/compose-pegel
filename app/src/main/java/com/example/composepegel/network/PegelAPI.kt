package com.example.composepegel.network

import com.example.composepegel.model.StationModel
import com.example.composepegel.model.WaterModel
import kotlinx.serialization.ExperimentalSerializationApi
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface PegelAPI {

    @GET("waters.json")
    suspend fun getWaters(): Response<List<WaterModel?>?>

    @ExperimentalSerializationApi
    @GET("stations.json")
    suspend fun getStationsForWater(@Query("waters") waterShortNames: String): Response<List<StationModel?>?>

    @ExperimentalSerializationApi
    @GET("stations/{uuid}.json")
    suspend fun getDetailsForStation(
        @Path("uuid") uuid: String,
        @Query("includeTimeseries") includeTimeseries: Boolean = true,
        @Query("includeCurrentMeasurement") includeCurrentMeasurement: Boolean = true
    ): Response<StationModel>
}