package com.example.composepegel.ui.map

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.navigation.NavController
import androidx.navigation.compose.navigate
import com.example.composepegel.model.StationModel
import com.example.composepegel.ui.common.DefaultError
import com.example.composepegel.ui.common.DefaultProgress
import com.example.composepegel.util.rememberMapViewWithLifecycle
import com.example.composepegel.util.setZoom
import com.google.android.libraries.maps.CameraUpdateFactory
import com.google.android.libraries.maps.MapView
import com.google.android.libraries.maps.model.LatLng
import com.google.android.libraries.maps.model.LatLngBounds
import com.google.maps.android.ktx.addMarker
import com.google.maps.android.ktx.awaitMap
import kotlinx.coroutines.launch
import org.koin.androidx.compose.getViewModel

@ExperimentalAnimationApi
@Composable
fun Map(
    navController: NavController,
    viewModel: MapViewModel = getViewModel()
) {
    val viewState by viewModel.state.observeAsState(MapState.InProgress)
    Box(modifier = Modifier.fillMaxSize()) {
        MapContent(
            state = viewState,
            onMarkerClicked = {
                navController.navigate("station/${it.uuid}?shortName=${it.shortname}")
            }
        )
    }
}

@ExperimentalAnimationApi
@Composable
fun MapContent(state: MapState, onMarkerClicked: (StationModel) -> (Unit)) {
    when (state) {
        MapState.InProgress -> DefaultProgress()
        is MapState.Stations -> CityMapView(stations = state.stations, onMarkerClicked)
        is MapState.Error -> DefaultError(error = state.error ?: "")
    }
}

@Composable
private fun CityMapView(stations: List<StationModel>, onMarkerClicked: (StationModel) -> (Unit)) {
    // The MapView lifecycle is handled by this composable. As the MapView also needs to be updated
    // with input from Compose UI, those updates are encapsulated into the MapViewContainer
    // composable. In this way, when an update to the MapView happens, this composable won't
    // recompose and the MapView won't need to be recreated.
    val mapView = rememberMapViewWithLifecycle()
    MapViewContainer(mapView, stations, onMarkerClicked)
}

@Composable
private fun MapViewContainer(
    map: MapView,
    stations: List<StationModel>,
    onMarkerClicked: (StationModel) -> (Unit)
) {
    var zoom by rememberSaveable { mutableStateOf(InitialZoom) }
    val coroutineScope = rememberCoroutineScope()
    AndroidView({ map }) { mapView ->
        // Reading zoom so that AndroidView recomposes when it changes. The getMapAsync lambda
        // is stored for later, Compose doesn't recognize state reads
        val mapZoom = zoom
        coroutineScope.launch {
            val googleMap = mapView.awaitMap()
            googleMap.setZoom(mapZoom)
            val latLngBounds = LatLngBounds.builder()
            stations.forEach {
                if (it.latitude != 0.0 && it.longitude != 0.0) {
                    val latLng = LatLng(it.latitude, it.longitude)
                    googleMap.addMarker {
                        position(latLng)
                        snippet(it.uuid)
                    }
                    latLngBounds.include(latLng)
                }
            }
            googleMap.setOnMarkerClickListener {
                val station = stations.find { itStation -> it.snippet == itStation.uuid }
                if (station != null) {
                    onMarkerClicked(station)
                    true
                } else {
                    false
                }
            }
            googleMap.moveCamera(CameraUpdateFactory.newLatLngBounds(latLngBounds.build(), 0))
        }
    }
    ZoomControls(zoom) {
        zoom = it.coerceIn(MinZoom, MaxZoom)
    }
}

@Composable
private fun ZoomControls(
    zoom: Float,
    onZoomChanged: (Float) -> Unit
) {
    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
        ZoomButton("-", onClick = { onZoomChanged(zoom * 0.8f) })
        ZoomButton("+", onClick = { onZoomChanged(zoom * 1.2f) })
    }
}

@Composable
private fun ZoomButton(text: String, onClick: () -> Unit) {
    Button(
        modifier = Modifier.padding(8.dp),
        colors = ButtonDefaults.buttonColors(
            backgroundColor = MaterialTheme.colors.onPrimary,
            contentColor = MaterialTheme.colors.primary
        ),
        onClick = onClick
    ) {
        Text(text = text, style = MaterialTheme.typography.h5)
    }
}

private const val InitialZoom = 5f
const val MinZoom = 2f
const val MaxZoom = 20f
