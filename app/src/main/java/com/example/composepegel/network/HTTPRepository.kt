package com.example.composepegel.network

import android.util.Log
import com.example.composepegel.model.StationModel
import com.example.composepegel.model.WaterModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import kotlinx.serialization.ExperimentalSerializationApi
import retrofit2.create

interface HTTPRepository {
    suspend fun getWaters(): Result<List<WaterModel>>

    suspend fun getStationsForWaters(waterShortname: String): Result<List<StationModel>>

    suspend fun getDetailForStation(stationUuid: String): Result<StationModel>
}

class HTTPRepositoryImpl(client: Client) : HTTPRepository {

    private val api = client.get().create<PegelAPI>()

    override suspend fun getWaters(): Result<List<WaterModel>> {
        return withContext(Dispatchers.Default) {
            // TODO Also build server model conversion
            delay(1500)
            try {
                val response = api.getWaters()
                Result.Success(data = response.body()!!.filterNotNull())
            } catch (e: Exception) {
                e.toResult()
            }
        }
    }

    override suspend fun getStationsForWaters(waterShortname: String): Result<List<StationModel>> {
        return withContext(Dispatchers.Default) {
            // TODO Also build server model conversion
            delay(1500)
            try {
                val response = api.getStationsForWater(waterShortname)
                Result.Success(data = response.body()!!.filterNotNull())
            } catch (e: Exception) {
                e.toResult()
            }
        }
    }

    override suspend fun getDetailForStation(stationUuid: String): Result<StationModel> {
        return withContext(Dispatchers.Default) {
            // TODO Also build server model conversion
            delay(1500)
            try {
                val response = api.getDetailsForStation(stationUuid)
                Result.Success(data = response.body()!!)
            } catch (e: Exception) {
                e.toResult()
            }
        }
    }
}