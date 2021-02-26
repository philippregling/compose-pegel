package com.example.composepegel.ui.station

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.composepegel.ui.common.DefaultError
import com.example.composepegel.ui.common.DefaultProgress

@Composable
fun Station(
    navController: NavController,
    stationUuid: String,
    viewModel: StationViewModel = viewModel(
        factory = stationViewModelFactory(stationUuid)
    )
) {
    val viewState by viewModel.state.observeAsState(StationState.InProgress)
    Box(modifier = Modifier.fillMaxSize()) {
        StationContent(state = viewState)
    }
}

@Composable()
fun StationContent(state: StationState) {
    when (state) {
        StationState.InProgress -> DefaultProgress()
        is StationState.Station -> {
            Text(
                text = state.station.timeseries.first().currentMeasurement?.timeStamp?.toString()
                    ?: ""
            )
        }
        is StationState.Error -> DefaultError(error = state.error ?: "")
    }
}
