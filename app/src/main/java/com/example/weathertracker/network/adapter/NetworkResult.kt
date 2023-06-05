package com.example.weathertracker.network.adapter

import com.example.weathertracker.network.adapter.Error as ErrorModel

sealed class NetworkResult<out M> {
    class Success<out M>(val data: M) : NetworkResult<M>()
    class Error(val data: ErrorModel? = null) : NetworkResult<Nothing>()
    class Failure(val message: String) : NetworkResult<Nothing>()
    object Loading : NetworkResult<Nothing>()
    object Empty : NetworkResult<Nothing>()
}

data class Error(
    val cod: String,
    val message: String
)
