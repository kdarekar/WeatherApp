package com.example.weathertracker.domain.models

import kotlin.collections.List as list

data class CurrentWeather(
    var base: String?,
    var clouds: Clouds?,
    var cod: Int?,
    var coord: Coord?,
    var dt: Int?,
    var id: Int?,
    var main: Main?,
    var name: String?,
    var sys: Sys?,
    var timezone: Int?,
    var visibility: Int?,
    var weather: list<Weather>?,
    var wind: Wind?
)