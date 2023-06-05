package com.example.weathertracker.di

import com.example.weathertracker.BuildConfig
import com.example.weathertracker.domain.models.CurrentWeather
import com.example.weathertracker.network.api.OpenWeatherApi
import com.example.weathertracker.network.mapper.CurrentWeatherMapper
import com.example.weathertracker.network.mapper.ResponseMapper
import com.example.weathertracker.network.model.CurrentWeatherResponse
import com.example.weathertracker.repository.OpenWeatherRepository
import com.example.weathertracker.repository.OpenWeatherRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataModule {
    private val okHttpClient: OkHttpClient by lazy {
        val interceptor = HttpLoggingInterceptor().apply {
            level = if (BuildConfig.DEBUG)
                HttpLoggingInterceptor.Level.BODY else
                HttpLoggingInterceptor.Level.NONE
        }
        OkHttpClient.Builder()
            .connectTimeout(15, TimeUnit.SECONDS)
            .readTimeout(1, TimeUnit.MINUTES)
            .addInterceptor(interceptor)
            .build()
    }

    private val baseUrl: String by lazy {
        BuildConfig.OPEN_WEATHER_BASE_URL
    }

    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()
    }

    @Provides
    @Singleton
    fun provideOpenWeatherApi(): OpenWeatherApi = retrofit.create(OpenWeatherApi::class.java)

    @Provides
    @Singleton
    fun provideCurrentWeatherMapper(): ResponseMapper<CurrentWeatherResponse, CurrentWeather> = CurrentWeatherMapper()

    @Provides
    @Singleton
    fun provideOpenWeatherRepository(
        openWeatherApi: OpenWeatherApi,
        currentWeatherMapper: ResponseMapper<CurrentWeatherResponse, CurrentWeather>
    ) : OpenWeatherRepository = OpenWeatherRepositoryImpl(openWeatherApi, currentWeatherMapper)
}