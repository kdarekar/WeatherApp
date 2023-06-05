package com.example.weathertracker.network.api

import com.example.weathertracker.network.model.CurrentWeatherResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface OpenWeatherApi {
    @GET("weather")
    suspend fun getWeatherByCity(
        @Query("q") city: String,
        @Query("appid") appid: String
    ): Response<CurrentWeatherResponse>
}
