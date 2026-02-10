package com.example.travelmate.data.remote.dto

data class CityDto(
    val id: Int,
    val name: String,
    val country: String,
    val latitude: Double,
    val longitude: Double,
    val population: Int
)
