package com.example.weathertracker.network.api

import android.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.weathertracker.domain.models.CurrentWeather
import com.example.weathertracker.network.mapper.CurrentWeatherMapper
import com.example.weathertracker.network.mapper.ResponseMapper
import com.example.weathertracker.network.model.CurrentWeatherResponse
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import okio.buffer
import okio.source
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@RunWith(JUnit4::class)
abstract class ApiBaseTest {

    @Rule
    @JvmField
    val instantExecutorRule = InstantTaskExecutorRule()

    protected lateinit var service: OpenWeatherApi

    protected lateinit var mockWebServer: MockWebServer

    protected lateinit var currentWeatherMapper: ResponseMapper<CurrentWeatherResponse, CurrentWeather>

    @Before
    fun createServiceAndRepository() {

        currentWeatherMapper = CurrentWeatherMapper()

        mockWebServer = MockWebServer()

        service = Retrofit.Builder()
            .baseUrl(mockWebServer.url("/"))
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(OpenWeatherApi::class.java)

    }

    @After
    fun stopService() {
        mockWebServer.shutdown()
    }

    protected fun enqueueResponse(
        fileName: String,
        code: Int = 200,
        headers: Map<String, String> = emptyMap()
    ) {
        val inputStream = javaClass.classLoader!!.getResourceAsStream(fileName)
        val source = inputStream.source().buffer()
        val mockResponse = MockResponse()
            .setResponseCode(code)
        for ((key, value) in headers)
            mockResponse.addHeader(key, value)
        mockWebServer.enqueue(mockResponse.setBody(source.readString(Charsets.UTF_8)))
    }

    protected inline fun <reified T : Any> readJsonResponse(fileName: String) : T {
        val fileContent = this::class.java.classLoader.getResource(fileName).readText()
        return Gson().fromJson(fileContent, object : TypeToken<T>() {}.type)
    }

}