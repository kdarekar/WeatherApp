package com.example.weathertracker.network.api

import kotlinx.coroutines.runBlocking
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.core.IsNull
import org.junit.Test

class OpenWeatherApiServiceTestApi: ApiBaseTest() {

    @Test
    fun `test current weather api`() = runBlocking {

        enqueueResponse("current_weather.json")
        val response = service.getWeatherByCity("query","api_key")
        val request = mockWebServer.takeRequest()

        assertThat(request.path, `is`("/weather?q=query&appid=api_key"))

        assertThat(response, IsNull.notNullValue())

        assertThat(response.code(), `is`(200))

        assertThat(response.body()?.base, `is`("stations"))
        assertThat(response.body()?.clouds?.all, `is`(25))

    }

    @Test
    fun `test error 400`() = runBlocking {

        enqueueResponse(fileName = "error.json", code = 400)
        val response = service.getWeatherByCity( "jsdkk","api_key")
        val request = mockWebServer.takeRequest()

        assertThat(request.path, `is`("/weather?q=jsdkk&appid=api_key"))

        assertThat(response, IsNull.notNullValue())

        assertThat(response.code(), `is`(400))

        assertThat(response.body(), IsNull.nullValue())

    }

}