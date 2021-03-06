package com.example.composepegel.ui.waters

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.composepegel.database.DatabaseClient
import com.example.composepegel.model.WaterModel
import com.example.composepegel.network.HTTPRepository
import com.example.composepegel.network.Result
import com.example.composepegel.util.NetworkStateClient
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WatersViewModel @Inject constructor(
    private val httpRepository: HTTPRepository,
    private val networkStateClient: NetworkStateClient,
    private val databaseClient: DatabaseClient
) : ViewModel() {




    private val _state = MutableLiveData<WatersState>(WatersState.InProgress)
    val state: LiveData<WatersState> = _state

    private val _query = MutableLiveData("")
    val query: LiveData<String> = _query

    init {
        viewModelScope.launch {
            when (val result = httpRepository.getWaters()) {
                is Result.Success -> {
                    launch(Dispatchers.Default) { databaseClient.saveWaters(result.data) }
                    _state.value = WatersState.Waters(result.data, false)
                }
                is Result.Error -> {
                    if (networkStateClient.isOffline) {
                        val offlineData = databaseClient.queryWaters()
                        _state.value = WatersState.Waters(offlineData, true)
                    } else {
                        _state.value = WatersState.Error(result.error)
                    }
                }

            }
        }
    }

    fun onQueryChanged(newQuery: String) {
        _query.value = newQuery
    }
}

sealed class WatersState {
    object InProgress : WatersState()
    data class Error(val error: String?) : WatersState()
    data class Waters(val waters: List<WaterModel>, val isCachedData: Boolean) : WatersState()
}
