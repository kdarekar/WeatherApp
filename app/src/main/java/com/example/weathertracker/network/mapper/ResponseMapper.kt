package com.example.weathertracker.network.mapper

interface ResponseMapper<I, O>  {
    fun mapToModel(entity: I): O
}