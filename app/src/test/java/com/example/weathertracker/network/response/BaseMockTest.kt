package com.example.weathertracker.network.response

import com.example.weathertracker.network.api.OpenWeatherApi
import com.example.weathertracker.network.mapper.CurrentWeatherMapper
import com.example.weathertracker.repository.OpenWeatherRepository
import com.example.weathertracker.repository.OpenWeatherRepositoryImpl
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import org.junit.Before
import org.mockito.Mock
import org.mockito.MockitoAnnotations

abstract class BaseMockTest {

    @Mock
    lateinit var openWeatherApi: OpenWeatherApi

    protected lateinit var openWeatherRepository: OpenWeatherRepository

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        openWeatherRepository = OpenWeatherRepositoryImpl(openWeatherApi, CurrentWeatherMapper())
    }

    protected inline fun <reified T : Any> readJsonResponse(fileName: String): T {
        val fileContent = this::class.java.classLoader.getResource(fileName).readText()
        return Gson().fromJson(fileContent, object : TypeToken<T>() {}.type)
    }

}