package com.example.composepegel.ui.station

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.composepegel.model.StationModel
import com.example.composepegel.network.HTTPRepository
import com.example.composepegel.network.Result
import kotlinx.coroutines.launch

class StationViewModel(
    private val httpRepository: HTTPRepository,
    stationUuid: String
) : ViewModel() {

    var state by mutableStateOf<StationState>(StationState.InProgress)
    private set

    init {
        viewModelScope.launch {
            state = when (val result = httpRepository.getDetailForStation(stationUuid)) {
                is Result.Success -> StationState.Station(result.data)
                is Result.Error -> StationState.Error(result.error)
            }
        }
    }
}

sealed class StationState {
    object InProgress : StationState()
    data class Error(val error: String?) : StationState()
    data class Station(val station: StationModel) : StationState()
}
