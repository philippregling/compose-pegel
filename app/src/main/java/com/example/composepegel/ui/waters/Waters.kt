package com.example.composepegel.ui.waters

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ScaffoldState
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldColors
import androidx.compose.material.TextFieldDefaults.textFieldColors
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.primarySurface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.navigate
import com.example.composepegel.R
import com.example.composepegel.model.WaterModel
import com.example.composepegel.ui.common.DefaultError
import com.example.composepegel.ui.common.DefaultProgress
import kotlinx.coroutines.launch
import org.koin.androidx.compose.getViewModel
import java.util.*

@ExperimentalFoundationApi
@Composable
fun Waters(
    navController: NavController,
    scaffoldState: ScaffoldState,
    viewModel: WatersViewModel = getViewModel()
) {
    val viewState by viewModel.state.observeAsState(WatersState.InProgress)
    val query by viewModel.query.observeAsState("")
    Box(modifier = Modifier.fillMaxSize()) {
        WatersContent(
            state = viewState,
            query = query,
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
            },
            onQueryChanged = { viewModel.onQueryChanged(it) }
        )
    }
}

@ExperimentalFoundationApi
@Composable
fun WatersContent(
    state: WatersState,
    query: String,
    scaffoldState: ScaffoldState,
    onWaterClicked: (water: WaterModel) -> Unit,
    onQueryChanged: (query: String) -> Unit,
) {
    when (state) {
        is WatersState.InProgress -> DefaultProgress()
        is WatersState.Waters -> {
            WatersList(
                waters = state.waters,
                query = query,
                onWaterClicked = onWaterClicked,
                onQueryChanged
            )
            val errorText = stringResource(id = R.string.using_cached_data)
            LaunchedEffect(key1 = "Cached", block = {
                if (state.isCachedData) scaffoldState.snackbarHostState.showSnackbar(
                    errorText,
                    "ok"
                )
            })
        }
        is WatersState.Error -> DefaultError(error = state.error ?: "")
    }
}

@ExperimentalFoundationApi
@Composable
fun WatersList(
    waters: List<WaterModel>,
    query: String,
    onWaterClicked: (water: WaterModel) -> Unit,
    onQueryChanged: (query: String) -> Unit
) {
    var text by remember { mutableStateOf(query) }
    LazyColumn(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = PaddingValues(vertical = 8.dp, horizontal = 8.dp)
    ) {
        stickyHeader("search") {
            Row(modifier = Modifier.fillMaxWidth()) {
                TextField(
                    value = text,
                    onValueChange = {
                        text = it
                    },
                    modifier = Modifier
                        .fillMaxWidth(),
                    label = { Text(text = "Search") },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Text,
                        imeAction = ImeAction.Done,
                    ),
                    leadingIcon = {
                        Icon(Icons.Filled.Search, contentDescription = "Search")
                    },
                    keyboardActions = KeyboardActions(onDone = {
                        onQueryChanged(text)
                    }),
                    textStyle = TextStyle(color = MaterialTheme.colors.onSurface),
                    colors = textFieldColors(
                        backgroundColor = MaterialTheme.colors.primarySurface
                    )
                )
            }
        }
        items(waters.filter {
            if (query.isNotBlank()) it.shortname.toLowerCase(Locale.ENGLISH)
                .contains(query.toLowerCase(Locale.ENGLISH)) else true
        }) { water ->
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


@ExperimentalFoundationApi
@Preview
@Composable
fun PreviewWatersList() {
    WatersList(
        waters = listOf(WaterModel("1", "1"), WaterModel("2", "2")),
        query = "",
        onWaterClicked = {},
        onQueryChanged = {})
}

@ExperimentalFoundationApi
@Preview(device = Devices.NEXUS_10)
@Composable
fun PreviewWatersList2() {
    WatersList(
        waters = listOf(WaterModel("1", "1"), WaterModel("2", "2")),
        query = "",
        onWaterClicked = {},
        onQueryChanged = {})
}

