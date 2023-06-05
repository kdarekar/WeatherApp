package com.example.weathertracker.repository

import com.example.weathertracker.BuildConfig
import com.example.weathertracker.domain.models.CurrentWeather
import com.example.weathertracker.network.adapter.NetworkResult
import com.example.weathertracker.network.api.OpenWeatherApi
import com.example.weathertracker.network.mapper.ResponseMapper
import com.example.weathertracker.network.model.CurrentWeatherResponse
import com.example.weathertracker.network.response.BaseResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class OpenWeatherRepositoryImpl @Inject constructor(
    private val openWeatherApi: OpenWeatherApi,
    private val currentWeatherMapper: ResponseMapper<CurrentWeatherResponse, CurrentWeather>
) : OpenWeatherRepository, BaseResponse() {

    override suspend fun fetchCurrentWeather(city: String): Flow<NetworkResult<CurrentWeather>> =
        flow {
            emit(NetworkResult.Loading)
            emit(safeApiCall(currentWeatherMapper) {
                openWeatherApi.getWeatherByCity(
                    city,
                    BuildConfig.OPEN_WEATHER_API_KEY
                )
            })
        }.flowOn(Dispatchers.IO)
}
