package com.example.composepegel.ui.map

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.composepegel.database.DatabaseClient
import com.example.composepegel.model.StationModel
import com.example.composepegel.model.WaterModel
import com.example.composepegel.network.HTTPRepository
import com.example.composepegel.network.Result
import com.example.composepegel.util.NetworkStateClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MapViewModel(
    private val httpRepository: HTTPRepository,
    private val networkStateClient: NetworkStateClient,
    private val databaseClient: DatabaseClient
) : ViewModel() {

    private val _state = MutableLiveData<MapState>(MapState.InProgress)
    val state: LiveData<MapState> = _state

    init {
        viewModelScope.launch {
            when (val result = httpRepository.getAllStations()) {
                is Result.Success -> {
                    launch(Dispatchers.Default) { databaseClient.saveStations(result.data) }
                    _state.value = MapState.Stations(result.data, false)
                }
                is Result.Error -> {
                    if (networkStateClient.isOffline) {
                        val offlineData = databaseClient.queryStations()
                        _state.value = MapState.Stations(offlineData, true)
                    } else {
                        _state.value = MapState.Error(result.error)
                    }
                }

            }
        }
    }
}

sealed class MapState {
    object InProgress : MapState()
    data class Error(val error: String?) : MapState()
    data class Stations(val stations: List<StationModel>, val isCachedData: Boolean) : MapState()
}
