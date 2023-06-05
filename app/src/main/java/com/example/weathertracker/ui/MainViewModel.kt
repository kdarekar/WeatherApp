package com.example.weathertracker.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weathertracker.domain.models.CurrentWeather
import com.example.weathertracker.network.adapter.NetworkResult
import com.example.weathertracker.repository.OpenWeatherRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: OpenWeatherRepository
) : ViewModel() {

    private val _weatherData = MutableStateFlow<NetworkResult<CurrentWeather>?>(NetworkResult.Empty)
    val weatherData: StateFlow<NetworkResult<CurrentWeather>?> = _weatherData

    fun fetchWeatherCall(city: String) {
        viewModelScope.launch {
            repository.fetchCurrentWeather(city).collect { result ->
                _weatherData.value = result
            }
        }
    }

}