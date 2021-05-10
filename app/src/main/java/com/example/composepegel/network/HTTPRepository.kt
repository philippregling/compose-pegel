package com.example.composepegel.network

import com.example.composepegel.model.StationModel
import com.example.composepegel.model.WaterModel
import com.example.composepegel.network.converters.convert
import com.example.composepegel.network.converters.convertStations
import com.example.composepegel.network.converters.convertWaters
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.components.ActivityRetainedComponent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import kotlinx.serialization.ExperimentalSerializationApi
import javax.inject.Inject

interface HTTPRepository {
    suspend fun getWaters(): Result<List<WaterModel>>

    suspend fun getAllStations(): Result<List<StationModel>>

    suspend fun getStationsForWaters(waterShortname: String): Result<List<StationModel>>

    suspend fun getDetailForStation(stationUuid: String): Result<StationModel>
}

class HTTPRepositoryImpl @Inject constructor(client: Client) : HTTPRepository {

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
                if (response.isSuccess()) {
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

@Module
@InstallIn(ActivityRetainedComponent::class)
abstract class HTTPRepositoryModule {

    @Binds
    abstract fun bindHTTPRepository(
        httpRepositoryImpl: HTTPRepositoryImpl
    ): HTTPRepository
}