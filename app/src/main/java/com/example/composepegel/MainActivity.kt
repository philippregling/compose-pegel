package com.example.composepegel

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.composepegel.model.WaterModel
import com.example.composepegel.ui.station.Station
import com.example.composepegel.ui.water.Water
import com.example.composepegel.ui.waters.Waters

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme(colors = if (isSystemInDarkTheme()) darkColors() else lightColors()) {
                val navController = rememberNavController()
                NavHost(navController = navController, startDestination = "waters") {
                    composable("waters") { Waters(navController) }
                    composable("water/{shortName}") {
                        Water(navController, it.arguments?.getString("shortName") ?: "")
                    }
                    composable("station/{uuid}") {
                        Station(navController, it.arguments?.getString("uuid") ?: "")
                    }
                }
            }
        }
    }
}