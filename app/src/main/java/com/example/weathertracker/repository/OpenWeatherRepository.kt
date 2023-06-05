package com.example.weathertracker.repository

import com.example.weathertracker.domain.models.CurrentWeather
import com.example.weathertracker.network.adapter.NetworkResult
import kotlinx.coroutines.flow.Flow

interface OpenWeatherRepository {
  suspend fun fetchCurrentWeather(city: String): Flow<NetworkResult<CurrentWeather>>

}