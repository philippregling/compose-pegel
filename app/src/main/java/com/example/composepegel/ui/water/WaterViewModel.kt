package com.example.composepegel.ui.water

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.composepegel.model.StationModel
import com.example.composepegel.network.HTTPRepository
import com.example.composepegel.network.Result
import kotlinx.coroutines.launch

class WaterViewModel(
    private val httpRepository: HTTPRepository,
    waterShortName: String
) : ViewModel() {

    private val _state = MutableLiveData<WaterState>(WaterState.InProgress)
    val state: LiveData<WaterState> = _state

    init {
        viewModelScope.launch {
            when (val result = httpRepository.getStationsForWaters(waterShortName)) {
                is Result.Success -> _state.value = WaterState.Stations(result.data)
                is Result.Error -> _state.value = WaterState.Error(result.error)
            }
        }
    }
}

sealed class WaterState {
    object InProgress : WaterState()
    data class Error(val error: String?) : WaterState()
    data class Stations(val stations: List<StationModel>) : WaterState()
}