package com.example.weathertracker.network.response

import com.example.weathertracker.network.adapter.NetworkResult
import com.example.weathertracker.network.mapper.ResponseMapper
import com.google.gson.Gson
import com.google.gson.JsonElement
import com.google.gson.JsonParser
import com.google.gson.annotations.SerializedName
import retrofit2.Response
import timber.log.Timber
import java.io.IOException
import com.example.weathertracker.network.adapter.Error as ErrorModel

abstract class BaseResponse {

    suspend fun <R, T> safeApiCall(
        responseMapper: ResponseMapper<R, T>,
        apiCall: suspend () -> Response<R>
    ): NetworkResult<T> {
        try {
            val response = apiCall()

            if (response.isSuccessful) {
                val body = response.body()
                body?.let {
                    Timber.d("response -> $body")
                    return NetworkResult.Success(responseMapper.mapToModel(body))
                }
            }
            val errorResponse = convertErrorBody(response)
            if (errorResponse != null) {
                Timber.d("errorResponse -> $errorResponse")
                return NetworkResult.Error(ErrorModel(errorResponse.cod, errorResponse.message))
            }
            Timber.d("Unknown Response")
            return NetworkResult.Failure("Unknown Response")
        } catch (e: Exception) {
            Timber.d("error message -> ${e.message ?: e.toString()}")
            return NetworkResult.Failure(e.message ?: e.toString())
        }
    }

    private fun <R> convertErrorBody(response: Response<R>): ErrorResponse? {
        val parser = JsonParser()
        val mJson: JsonElement?
        return try {
            mJson = parser.parse(response.errorBody()?.string())
            val gson = Gson()
            gson.fromJson(mJson, ErrorResponse::class.java)
        } catch (ex: IOException) {
            null
        }
    }
}

data class ErrorResponse(
    @SerializedName("cod") val cod : String,
    @SerializedName("message") val message : String
)
