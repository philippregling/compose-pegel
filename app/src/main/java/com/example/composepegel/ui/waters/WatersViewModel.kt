package com.example.composepegel.ui.waters

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.composepegel.model.WaterModel
import com.example.composepegel.network.ClientImpl
import com.example.composepegel.network.HTTPRepository
import com.example.composepegel.network.HTTPRepositoryImpl
import com.example.composepegel.network.Result
import kotlinx.coroutines.launch

class WatersViewModel() : ViewModel() {

    // TODO Dependency Injection does not work yet, try koin ina  few days
    private val httpRepository: HTTPRepository = HTTPRepositoryImpl(ClientImpl())

    private val _state = MutableLiveData<WatersState>(WatersState.InProgress)
    val state: LiveData<WatersState> = _state

    init {
        viewModelScope.launch {
            when (val result = httpRepository.getWaters()) {
                is Result.Success -> _state.value = WatersState.Waters(result.data)
                is Result.Error -> _state.value = WatersState.Error(result.error)
            }
        }
    }
}

sealed class WatersState {
    object InProgress : WatersState()
    data class Error(val error: String?) : WatersState()
    data class Waters(val waters: List<WaterModel>) : WatersState()
}
