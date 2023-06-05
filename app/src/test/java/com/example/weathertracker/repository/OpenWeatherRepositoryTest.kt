package com.example.weathertracker.repository

import com.example.weathertracker.domain.models.CurrentWeather
import com.example.weathertracker.network.adapter.NetworkResult
import com.example.weathertracker.network.model.CurrentWeatherResponse
import com.example.weathertracker.network.response.BaseMockTest
import kotlinx.coroutines.runBlocking
import org.hamcrest.CoreMatchers
import org.hamcrest.MatcherAssert
import org.junit.Test
import org.mockito.ArgumentMatchers.anyString
import org.mockito.Mockito.`when`
import retrofit2.Response

open class OpenWeatherRepositoryTest : BaseMockTest() {

    @Test
    fun `test fetch current weather`() = runBlocking {

        val currentWeatherResponse = readJsonResponse<CurrentWeatherResponse>("current_weather.json")

        `when`(openWeatherApi.getWeatherByCity(anyString(), anyString())).thenReturn(Response.success(currentWeatherResponse))

        openWeatherRepository.fetchCurrentWeather("city").collect { value: NetworkResult<CurrentWeather> ->

            if(value is NetworkResult.Success) {
                MatcherAssert.assertThat(
                    value.data?.base,
                    CoreMatchers.`is`("stations")
                )
            }

        }

    }

}