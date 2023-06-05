package com.example.weathertracker.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.weathertracker.location.LocationInfo
import com.example.weathertracker.ui.screens.EnableGpsContent
import com.example.weathertracker.ui.screens.LoadingContent
import com.example.weathertracker.ui.screens.WeatherContent
import com.example.weathertracker.ui.theme.WeatherTrackerTheme
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.InternalCoroutinesApi

@ExperimentalPermissionsApi
@ExperimentalAnimationApi
@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val viewModel: MainViewModel by viewModels()

    @OptIn(InternalCoroutinesApi::class, androidx.compose.material.ExperimentalMaterialApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val navController = rememberNavController()

            // Weather navigation callback
            val navigateToWeather: (LocationInfo) -> Unit = { locationInfo ->
                navController.navigate("weather/${locationInfo.location}") {
                    launchSingleTop = true
                    popUpTo("loading") { inclusive = true }
                }
            }

            WeatherTrackerTheme {
                NavHost(navController, startDestination = "loading") {

                    composable(route = "loading") {
                        LoadingContent(
                            navigateToWeather
                        )
                    }

                    composable(route = "weather/{location}") { backStackEntry ->
                        val location = backStackEntry.arguments?.getString("location")
                        WeatherContent(
                            viewModel,
                            location!!
                        )
                    }
                }
            }

        }

    }

}

@ExperimentalPermissionsApi
@ExperimentalAnimationApi
@Preview(showBackground = true)
@Composable
fun MainPreview() {
    WeatherTrackerTheme {
        EnableGpsContent()
    }
}