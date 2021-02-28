package com.example.composepegel.ui.waters

import android.view.animation.AnimationUtils
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Card
import androidx.compose.material.ScaffoldState
import androidx.compose.material.Snackbar
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.navigate
import com.example.composepegel.R
import com.example.composepegel.architecture.getViewModel
import com.example.composepegel.model.WaterModel
import com.example.composepegel.ui.common.DefaultError
import com.example.composepegel.ui.common.DefaultProgress
import kotlinx.coroutines.launch

@Composable
fun Waters(
    navController: NavController,
    scaffoldState: ScaffoldState,
    viewModel: WatersViewModel = getViewModel()
) {
    val viewState by viewModel.state.observeAsState(WatersState.InProgress)
    Box(modifier = Modifier.fillMaxSize()) {
        WatersContent(
            state = viewState,
            scaffoldState = scaffoldState,
            onWaterClicked = {
                navController.navigate("water/${it.shortname}") {
                    anim {
                        enter = R.anim.anim_slide_in_right
                        exit = R.anim.anim_slide_out_left
                        popEnter = R.anim.anim_slide_in_left
                        popExit = R.anim.anim_slide_out_right
                    }
                }
            }
        )
    }
}

@Composable
fun WatersContent(
    state: WatersState,
    scaffoldState: ScaffoldState,
    onWaterClicked: (water: WaterModel) -> Unit
) {
    when (state) {
        WatersState.InProgress -> DefaultProgress()
        is WatersState.Waters -> {
            WatersList(waters = state.waters, onWaterClicked = onWaterClicked)
            val errorText = stringResource(id = R.string.using_cached_data)
            rememberCoroutineScope().launch {
                if (state.isCachedData) scaffoldState.snackbarHostState.showSnackbar(
                    errorText,
                    "ok"
                )
            }
        }
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
                    Text(text = water.shortname)
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

