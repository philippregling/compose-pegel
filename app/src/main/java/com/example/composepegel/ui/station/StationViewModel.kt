package com.example.composepegel.ui.station

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.composepegel.architecture.viewModelFactory
import com.example.composepegel.model.StationModel
import com.example.composepegel.network.ClientImpl
import com.example.composepegel.network.HTTPRepository
import com.example.composepegel.network.HTTPRepositoryImpl
import com.example.composepegel.network.Result
import kotlinx.coroutines.launch

class StationViewModel(stationUuid: String) : ViewModel() {

    // TODO Dependency Injection does not work yet, try koin ina  few days
    private val httpRepository: HTTPRepository = HTTPRepositoryImpl(ClientImpl())

    private val _state = MutableLiveData<StationState>(StationState.InProgress)
    val state: LiveData<StationState> = _state

    init {
        viewModelScope.launch {
            when (val result = httpRepository.getDetailForStation(stationUuid)) {
                is Result.Success -> _state.value = StationState.Station(result.data)
                is Result.Error -> _state.value = StationState.Error(result.error)
            }
        }
    }
}

sealed class StationState {
    object InProgress : StationState()
    data class Error(val error: String?) : StationState()
    data class Station(val station: StationModel) : StationState()
}

fun stationViewModelFactory(stationUuid: String) = viewModelFactory {
    StationViewModel(stationUuid)
}
