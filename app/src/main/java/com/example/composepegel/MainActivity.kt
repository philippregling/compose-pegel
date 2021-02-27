package com.example.composepegel

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.darkColors
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.lightColors
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.composepegel.network.SimpleCache
import com.example.composepegel.ui.station.Station
import com.example.composepegel.ui.water.Water
import com.example.composepegel.ui.waters.Waters
import com.example.composepegel.util.shareStationDetails
import dev.chrisbanes.accompanist.insets.ProvideWindowInsets

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()
            MaterialTheme(colors = if (isSystemInDarkTheme()) darkColors() else lightColors()) {
                val titleState = mutableStateOf("")
                val menuState = mutableStateOf(MenuState())
                ProvideWindowInsets {
                    Scaffold(
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
                        content = {
                            NavHost(navController = navController, startDestination = "waters") {
                                composable("waters") {
                                    titleState.value = "Waters"
                                    menuState.value = MenuState()
                                    Waters(navController)
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
                            }
                        })
                }
            }
        }
    }
}


data class MenuState(
    var showBack: Boolean = false,
    var showShare: Boolean = false,
)