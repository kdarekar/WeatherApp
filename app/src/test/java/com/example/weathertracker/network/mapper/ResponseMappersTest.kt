package com.example.weathertracker.network.mapper

import com.example.weathertracker.network.api.ApiBaseTest
import com.example.weathertracker.network.model.CurrentWeatherResponse
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Test

class ResponseMappersTest: ApiBaseTest() {

    @Test
    fun `test current weather mapper`() {

        val currentWeatherResponse = readJsonResponse<CurrentWeatherResponse>("current_weather.json")
        val currentWeather = currentWeatherMapper.mapToModel(currentWeatherResponse)

        assertThat(currentWeatherResponse.base, `is`(currentWeather.base))

    }

}