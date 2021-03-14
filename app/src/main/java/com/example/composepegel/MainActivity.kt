package com.example.composepegel

import android.os.Bundle
import android.util.Log
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Snackbar
import androidx.compose.material.SnackbarHost
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.darkColors
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Waves
import androidx.compose.material.lightColors
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigate
import androidx.navigation.compose.rememberNavController
import com.example.composepegel.network.SimpleCache
import com.example.composepegel.ui.map.Map
import com.example.composepegel.ui.station.Station
import com.example.composepegel.ui.water.Water
import com.example.composepegel.ui.waters.Waters
import com.example.composepegel.util.NetworkStateClient
import com.example.composepegel.util.shareStationDetails
import dev.chrisbanes.accompanist.insets.ProvideWindowInsets
import org.koin.android.ext.android.inject


class MainActivity : AppCompatActivity() {

    private val networkStateClient: NetworkStateClient by inject()

    @ExperimentalFoundationApi
    @ExperimentalAnimationApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        networkStateClient.register(this)
        setContent {
            val navController = rememberNavController()
            MaterialTheme(colors = if (isSystemInDarkTheme()) darkColors() else lightColors()) {
                val titleState = mutableStateOf("")
                val menuState = mutableStateOf(MenuState())
                val scaffoldState = rememberScaffoldState()
                val bottomNavState = mutableStateOf(0)
                ProvideWindowInsets {
                    Scaffold(
                        scaffoldState = scaffoldState,
                        topBar = {
                            TopAppBar(
                                elevation = 8.dp,
                                title = { Text(text = titleState.value) },
                                navigationIcon = if (menuState.value.showBack) {
                                    {
                                        IconButton(onClick = {
                                            navController.navigateUp()
                                        }
                                        ) {
                                            Icon(
                                                Icons.Filled.ArrowBack,
                                                contentDescription = stringResource(id = R.string.back)
                                            )
                                        }
                                    }
                                } else null,
                                modifier = Modifier.fillMaxWidth(),
                                actions = if (menuState.value.showShare) {
                                    {
                                        IconButton(onClick = {
                                            SimpleCache.lastLoadedStation?.let {
                                                shareStationDetails(
                                                    it
                                                )
                                            }
                                        }) {
                                            Icon(
                                                Icons.Filled.Share,
                                                contentDescription = stringResource(id = R.string.share)
                                            )
                                        }
                                    }
                                } else {
                                    {}
                                }
                            )
                        },
                        bottomBar = {
                            BottomNavigation(modifier = Modifier.fillMaxWidth()) {
                                BottomNavigationItem(selected = bottomNavState.value == 0,
                                    onClick = {
                                        bottomNavState.value = 0
                                        navController.navigate("waters")
                                    }, label = {
                                        Text(text = stringResource(id = R.string.waters))
                                    }, icon = {
                                        Icon(
                                            Icons.Filled.Waves,
                                            contentDescription = stringResource(id = R.string.waters)
                                        )
                                    })
                                BottomNavigationItem(
                                    selected = bottomNavState.value == 1,
                                    onClick = {
                                        bottomNavState.value = 1
                                        navController.navigate("map")
                                    },
                                    label = {
                                        Text(text = stringResource(id = R.string.map))
                                    },
                                    icon = {
                                        Icon(
                                            Icons.Filled.Map,
                                            contentDescription = stringResource(id = R.string.map)
                                        )
                                    })
                            }
                        },
                        content = {
                            NavHost(navController = navController, startDestination = "waters") {
                                composable("waters") {
                                    titleState.value = "Waters"
                                    menuState.value = MenuState()
                                    Waters(navController, scaffoldState)
                                }
                                composable("water/{shortName}") {
                                    val shortName = it.arguments?.getString("shortName") ?: ""
                                    titleState.value = shortName
                                    menuState.value = MenuState(showBack = true)
                                    Water(navController, shortName)
                                }
                                composable("station/{uuid}?shortName={shortName}") {
                                    val shortName = it.arguments?.getString("shortName") ?: ""
                                    titleState.value = shortName
                                    menuState.value = MenuState(showBack = true, showShare = true)
                                    Station(
                                        navController,
                                        it.arguments?.getString("uuid") ?: ""
                                    )
                                }
                                composable("map") {
                                    titleState.value = "Map"
                                    menuState.value = MenuState()
                                    Map(navController)
                                }
                            }
                        },
                        snackbarHost = {
                            SnackbarHost(
                                it,
                                snackbar = { data ->
                                    Snackbar(data, elevation = 8.dp)
                                })
                        })
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        networkStateClient.unregister(this)
    }
}


data class MenuState(
    var showBack: Boolean = false,
    var showShare: Boolean = false,
)