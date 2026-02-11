package com.example.travelmate.data.remote

import com.example.travelmate.data.remote.dto.CityResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface GeoDbApi {

    @GET("v1/geo/cities")
    suspend fun getCities(
        @Query("limit") limit: Int = 20,
        @Query("offset") offset: Int = 0
    ): CityResponse

    @GET("v1/geo/cities")
    suspend fun searchCities(
        @Query("namePrefix") query: String,
        @Query("limit") limit: Int = 20
    ): CityResponse
}