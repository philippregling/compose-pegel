package com.example.composepegel.ui.station

import android.util.Base64
import android.util.Base64.NO_WRAP
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Card
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.bumptech.glide.request.RequestOptions
import com.example.composepegel.R
import com.example.composepegel.model.StationModel
import com.example.composepegel.ui.common.DefaultError
import com.example.composepegel.ui.common.DefaultProgress
import com.example.composepegel.util.generateMeasurementsUrl
import dev.chrisbanes.accompanist.glide.GlideImage
import jp.wasabeef.glide.transformations.gpu.InvertFilterTransformation
import org.koin.androidx.compose.getViewModel
import org.koin.core.parameter.parametersOf
import java.util.*


@ExperimentalAnimationApi
@Composable
fun Station(
    navController: NavController,
    stationUuid: String,
    viewModel: StationViewModel = getViewModel { parametersOf(stationUuid) }
) {
    Box(modifier = Modifier.fillMaxSize()) {
        StationContent(state = viewModel.state)
    }
}

@ExperimentalAnimationApi
@Composable
fun StationContent(state: StationState) {
    when (state) {
        StationState.InProgress -> DefaultProgress()
        is StationState.Station -> StationDetails(station = state.station)
        is StationState.Error -> DefaultError(error = state.error ?: "")
    }
}

@ExperimentalAnimationApi
@Composable
fun StationDetails(station: StationModel) {
    Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
        val darkTheme = isSystemInDarkTheme()
        GlideImage(
            data = generateMeasurementsUrl(station.uuid, "W"),
            loading = {
                Box(Modifier.matchParentSize()) {
                    CircularProgressIndicator(Modifier.align(Alignment.Center))
                }
            },
            contentDescription = null,
            requestBuilder = {
                if (darkTheme) {
                    val options = RequestOptions().transform(InvertFilterTransformation())
                    apply(options)
                } else {
                    val options = RequestOptions()
                    apply(options)
                }
            },
            modifier = Modifier.fillMaxWidth().animateContentSize()
        )
        StationInfo(station)
    }
}

class StationInfoCardState(expanded: Boolean) {
    var expanded: Boolean by mutableStateOf(expanded)
}

@ExperimentalAnimationApi
@Composable
fun StationInfo(
    station: StationModel,
    expandState: StationInfoCardState = remember { StationInfoCardState(true) }
) {
    Card(Modifier.padding(8.dp)) {
        Column(
            Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            AnimatedVisibility(visible = expandState.expanded) {
                Column(Modifier.fillMaxWidth()) {
                    Text(
                        text = stringResource(id = R.string.station),
                        style = MaterialTheme.typography.h4
                    )
                    StationInfoElement(
                        title = stringResource(id = R.string.uuid),
                        value = station.uuid
                    )
                    StationInfoElement(
                        title = stringResource(id = R.string.shortname),
                        value = station.shortname
                    )
                    StationInfoElement(
                        title = stringResource(id = R.string.longname),
                        value = station.longname
                    )
                    StationInfoElement(
                        title = stringResource(id = R.string.km),
                        value = stringResource(id = R.string.km_x, station.km.toString())
                    )
                    StationInfoElement(
                        title = stringResource(id = R.string.agency),
                        value = station.agency,
                        isLastItem = true
                    )
                    Text(
                        modifier = Modifier.padding(top = 16.dp),
                        text = stringResource(id = R.string.water),
                        style = MaterialTheme.typography.h4
                    )
                    StationInfoElement(
                        title = stringResource(id = R.string.shortname),
                        value = station.water?.shortname ?: ""
                    )
                    StationInfoElement(
                        title = stringResource(id = R.string.longname),
                        value = station.water?.longname ?: "", isLastItem = true
                    )
                }
            }
            IconButton(
                onClick = { expandState.expanded = !expandState.expanded },
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(
                    if (expandState.expanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                    contentDescription = if (expandState.expanded) stringResource(id = R.string.collapse) else stringResource(
                        id = R.string.expand
                    )
                )
            }
        }
    }
}

@Composable
fun StationInfoElement(
    title: String,
    value: String,
    isLastItem: Boolean = false
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 8.dp, bottom = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.h6,
            modifier = Modifier.padding(end = 128.dp)
        )
        Text(text = value)
    }
    if (!isLastItem) Divider()
}

@ExperimentalAnimationApi
@Preview
@Composable
fun StationDetailsPreview() {
    StationDetails(station = StationModel(uuid = "593647aa-9fea-43ec-a7d6-6476a76ae868"))
}
