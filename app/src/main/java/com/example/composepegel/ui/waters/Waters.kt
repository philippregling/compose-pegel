package com.example.composepegel.ui.waters

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
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.navigate
import com.example.composepegel.model.WaterModel
import com.example.composepegel.ui.common.DefaultError
import com.example.composepegel.ui.common.DefaultProgress

@Composable
fun Waters(navController: NavController, viewModel: WatersViewModel = viewModel()) {
    val viewState by viewModel.state.observeAsState(WatersState.InProgress)
    Box(modifier = Modifier.fillMaxSize()) {
        WatersContent(
            state = viewState,
            onWaterClicked = { navController.navigate("water/${it.shortname}") }
        )
    }
}

@Composable()
fun WatersContent(state: WatersState, onWaterClicked: (water: WaterModel) -> Unit) {
    when (state) {
        WatersState.InProgress -> DefaultProgress()
        is WatersState.Waters -> WatersList(waters = state.waters, onWaterClicked = onWaterClicked)
        is WatersState.Error -> DefaultError(error = state.error ?: "")
    }
}

@Composable
fun WatersList(waters: List<WaterModel>, onWaterClicked: (water: WaterModel) -> Unit) {
    LazyColumn(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = PaddingValues(vertical = 8.dp, horizontal = 8.dp)
    ) {
        items(waters) { water ->
            Card(modifier = Modifier
                .fillMaxWidth()
                .clickable {
                    onWaterClicked(water)
                }) {
                Box(modifier = Modifier.padding(16.dp)) {
                    Text(text = water.shortname ?: "")
                }
            }
        }
    }
}



@Preview
@Composable
fun PreviewWatersList() {
    WatersList(waters = listOf(WaterModel("1", "1"), WaterModel("2", "2")), onWaterClicked = {})
}

