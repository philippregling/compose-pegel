package com.example.composepegel.network

import com.example.composepegel.model.StationModel
import com.example.composepegel.model.WaterModel
import com.example.composepegel.network.converters.convert
import com.example.composepegel.network.converters.convertStations
import com.example.composepegel.network.converters.convertWaters
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import kotlinx.serialization.ExperimentalSerializationApi

interface HTTPRepository {
    suspend fun getWaters(): Result<List<WaterModel>>

    suspend fun getAllStations(): Result<List<StationModel>>

    suspend fun getStationsForWaters(waterShortname: String): Result<List<StationModel>>

    suspend fun getDetailForStation(stationUuid: String): Result<StationModel>
}

class HTTPRepositoryImpl(client: Client) : HTTPRepository {

    private val api = client.get()

    override suspend fun getWaters(): Result<List<WaterModel>> {
        return withContext(Dispatchers.Default) {
            delay(500)
            try {
                val response = api.getWaters()
                if (response.isSuccess()) {
                    Result.Success(data = response.body()!!.filterNotNull().convertWaters())
                } else {
                    Result.Error(error = response.getError())
                }
            } catch (e: Exception) {
                e.toResult()
            }
        }
    }

    @ExperimentalSerializationApi
    override suspend fun getAllStations(): Result<List<StationModel>> {
        return withContext(Dispatchers.Default) {
            delay(500)
            try {
                val response = api.getStations()
                if(response.isSuccess()){
                    Result.Success(data = response.body()!!.filterNotNull().convertStations())
                } else {
                    Result.Error(error = response.getError())
                }
            } catch (e: Exception) {
                e.toResult()
            }
        }
    }

    @ExperimentalSerializationApi
    override suspend fun getStationsForWaters(waterShortname: String): Result<List<StationModel>> {
        return withContext(Dispatchers.Default) {
            delay(500)
            try {
                val response = api.getStationsForWater(waterShortname)
                if (response.isSuccess()) {
                    Result.Success(data = response.body()!!.filterNotNull().convertStations())
                } else {
                    Result.Error(response.getError())
                }
            } catch (e: Exception) {
                e.toResult()
            }
        }
    }

    @ExperimentalSerializationApi
    override suspend fun getDetailForStation(stationUuid: String): Result<StationModel> {
        return withContext(Dispatchers.Default) {
            delay(500)
            try {
                val response = api.getDetailsForStation(stationUuid)
                if (response.isSuccess()) {
                    val station = response.body()!!.convert()
                    SimpleCache.lastLoadedStation = station
                    Result.Success(data = station)
                } else {
                    Result.Error(response.getError())
                }
            } catch (e: Exception) {
                e.toResult()
            }
        }
    }
}