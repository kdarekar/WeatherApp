package com.example.weathertracker.util

import java.text.SimpleDateFormat
import java.util.Calendar
import kotlin.math.roundToInt

fun Double.toTemperature(unit: String= "°C") : String {
    return when (unit) {
        "°C" -> {
            var celcius = this - 273.15
            "${celcius.roundToInt()} °C"
        }
        "°F" -> {
            var fahrenheit = (9/5*(this - 273)) + 32
            "${fahrenheit.roundToInt()} °F"
        }
        else -> {""}
    }
}

fun Double.toSpeed(unit: String = "m/s") : String {
    return when (unit) {
        "m/s" -> "${this.roundToInt()} m/s"
        "km/h" -> {
            var speed = this*3.6
            "${speed.roundToInt()} km/h"
        }
        "mph" -> {
            var speed = this*2.23694
            "${speed.roundToInt()} mph"
        }
        else -> {""}
    }
}

fun Int.toDirection() : String = "${this}°"

fun Int.toPressure(unit: String="hPa") : String {
    return when (unit) {
        "hPa" -> "$this hPa"
        "inHg" -> {
            var pressure = this*0.029529983071445
            "${pressure.roundToInt()} inHg"
        }
        else -> {""}
    }
}

fun Int.toVisibility() : String = "$this m"

fun Int.toHumidity() : String = "$this %"

fun Int.toDate() : String {
    val seconds = this*1000L
    val calendar = Calendar.getInstance()
    calendar.timeInMillis = seconds
    val formatter = SimpleDateFormat("EEE, d MMM yyyy")
    return formatter.format(calendar.time)
}