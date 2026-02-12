package com.example.travelmate.domain.repository

import com.example.travelmate.domain.model.Place
import kotlinx.coroutines.flow.Flow

interface PlaceRepository {

    fun getPlaces(): Flow<List<Place>>

    suspend fun searchPlaces(query: String): List<Place>

    suspend fun getPlaceById(id: String): Place

    suspend fun toggleFavorite(placeId: String)

    suspend fun refreshCities(offset: Int, limit: Int = 20)
}
