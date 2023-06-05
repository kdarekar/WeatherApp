package com.example.weathertracker.ui.screens

import android.Manifest
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import com.example.weathertracker.location.LocationInfo
import com.example.weathertracker.location.LocationService
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState

@ExperimentalPermissionsApi
@Composable
fun LoadingContent(
    navigateToWeather: (LocationInfo) -> Unit
){

    val context = LocalContext.current

    var doNotShowRationale by rememberSaveable { mutableStateOf(false) }

    // Create permission state or required permissions
    val locationPermissionState = rememberMultiplePermissionsState(
        listOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION)
    )

    when {
        // Permissions granted
        locationPermissionState.allPermissionsGranted -> {
            // Create location service and observe gps coordinates and navigate to weather
            // content else prompt user to enable device location services
            val locationService = LocationService(context)
            locationService.locationInfo.value?.let { locationInfo ->
                navigateToWeather(locationInfo)
            } ?: run {
                EnableGpsContent()
            }
        }
        //Either location services no available or requested permissions not granted
        locationPermissionState.shouldShowRationale ||
                !locationPermissionState.permissionRequested -> {
            if (doNotShowRationale) {
                NoGpsContent()
            } else {
                // Ask user to grant ermissions
                PermissionContent {
                    locationPermissionState.launchMultiplePermissionRequest()
                }
            }
        }
        // User denied permissions, request explicit permission access
        else -> {
            RationaleContent()
        }
    }

}

