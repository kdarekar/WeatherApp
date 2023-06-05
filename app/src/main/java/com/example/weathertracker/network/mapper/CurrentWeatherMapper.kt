package com.example.weathertracker.network.mapper

import com.example.weathertracker.domain.models.Clouds
import com.example.weathertracker.domain.models.Coord
import com.example.weathertracker.domain.models.CurrentWeather
import com.example.weathertracker.domain.models.Main
import com.example.weathertracker.domain.models.Sys
import com.example.weathertracker.domain.models.Weather
import com.example.weathertracker.domain.models.Wind
import com.example.weathertracker.network.model.CurrentWeatherResponse

class CurrentWeatherMapper : ResponseMapper<CurrentWeatherResponse, CurrentWeather> {
    override fun mapToModel(response: CurrentWeatherResponse): CurrentWeather {
        return CurrentWeather(
            response.base,
            Clouds(
                response.clouds?.all
            ),
            response.cod,
            Coord(
                response.coord?.lon,
                response.coord?.lat
            ),
            response.dt,
            response.id,
            Main(
                response.main?.feelsLike,
                response.main?.humidity,
                response.main?.pressure,
                response.main?.temp,
                response.main?.tempMax,
                response.main?.tempMin,
            ),
            response.name,
            Sys(
                response.sys?.country,
                response.sys?.id,
                response.sys?.sunrise,
                response.sys?.sunset,
                response.sys?.type
            ),
            response.timezone,
            response.visibility,
            response.weather?.map { weather ->
                Weather(
                    weather?.description,
                    weather?.icon,
                    weather?.id,
                    weather?.main,
                )
            },
            Wind(
                response.wind?.deg,
                response.wind?.gust,
                response.wind?.speed
            ),
        )
    }
}

