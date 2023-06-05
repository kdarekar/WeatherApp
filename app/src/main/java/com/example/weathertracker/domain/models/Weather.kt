package com.example.weathertracker.domain.models

data class Weather(
    var description: String?,
    var icon: String?,
    var id: Int?,
    var main: String?
)