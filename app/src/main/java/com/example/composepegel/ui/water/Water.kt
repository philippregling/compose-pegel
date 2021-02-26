package com.example.composepegel.ui.water

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.navigate
import com.example.composepegel.model.StationModel
import com.example.composepegel.ui.common.DefaultError
import com.example.composepegel.ui.common.DefaultProgress

@Composable
fun Water(
    navController: NavController,
    waterShortName: String,
    viewModel: WaterViewModel = viewModel(factory = waterViewModelFactory(waterShortName))
) {
    val viewState by viewModel.state.observeAsState(WaterState.InProgress)
    Box(modifier = Modifier.fillMaxSize()) {
        WaterContent(
            state = viewState,
            onStationsClicked = { navController.navigate("station/${it.uuid}?shortName=${it.shortname}") }
        )
    }
}

@Composable()
fun WaterContent(state: WaterState, onStationsClicked: (station: StationModel) -> Unit) {
    when (state) {
        WaterState.InProgress -> DefaultProgress()
        is WaterState.Stations -> StationsList(
            stations = state.stations,
            onStationClicked = onStationsClicked
        )
        is WaterState.Error -> DefaultError(error = state.error ?: "")
    }
}

@Composable
fun StationsList(stations: List<StationModel>, onStationClicked: (station: StationModel) -> Unit) {
    LazyColumn(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = PaddingValues(vertical = 8.dp, horizontal = 8.dp)
    ) {
        items(stations) { station ->
            Card(modifier = Modifier
                .fillMaxWidth()
                .clickable {
                    onStationClicked(station)
                }) {
                Box(modifier = Modifier.padding(16.dp)) {
                    Text(text = station.shortname ?: "")
                }
            }
        }
    }
}

@Preview
@Composable
fun PreviewStationsList() {
    StationsList(
        stations = listOf(
            StationModel("1", "1", "1", "1", 1.0, "1"),
            StationModel("2", "2", "2", "2", 2.0, "2")
        ),
        onStationClicked = {})
}