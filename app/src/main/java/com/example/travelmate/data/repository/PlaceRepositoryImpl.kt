package com.example.travelmate.data.repository

import com.example.travelmate.data.local.CityDao
import com.example.travelmate.data.mapper.toDomain
import com.example.travelmate.data.mapper.toEntity
import com.example.travelmate.data.remote.GeoDbApi
import com.example.travelmate.domain.model.Place
import com.example.travelmate.domain.repository.PlaceRepository
import com.example.travelmate.data.remote.review.ReviewRemoteDataSource
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class PlaceRepositoryImpl @Inject constructor(
    private val api: GeoDbApi,
    private val dao: CityDao,
    private val reviewDataSource: ReviewRemoteDataSource
) : PlaceRepository {

    private val database = FirebaseDatabase.getInstance().getReference("places")

    override fun getPlaces(): Flow<List<Place>> {
        val placesFlow = callbackFlow {
            val listener = object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val items = snapshot.children.mapNotNull { it.getValue(Place::class.java) }
                    trySend(items)
                }
                override fun onCancelled(error: DatabaseError) {
                    close(error.toException())
                }
            }
            database.addValueEventListener(listener)
            awaitClose { database.removeEventListener(listener) }
        }

        return combine(placesFlow, reviewDataSource.observeAllReviews()) { places, reviews ->
            places.map { place ->
                val placeReviews = reviews.filter { it.placeId == place.id }
                val avgRating = if (placeReviews.isNotEmpty()) {
                    placeReviews.map { it.rating }.average()
                } else {
                    place.rating // fallback to initial
                }
                place.copy(rating = Math.round(avgRating * 10.0) / 10.0)
            }
        }
    }

    override suspend fun searchPlaces(query: String): List<Place> {
        val firebasePlaces = getPlaces().first()
        val filtered = firebasePlaces.filter { 
            it.name.contains(query, ignoreCase = true) || 
            it.city.contains(query, ignoreCase = true) ||
            it.country.contains(query, ignoreCase = true)
        }
        
        if (filtered.isNotEmpty()) return filtered

        val response = try { api.searchCities(query) } catch (e: Exception) { null }
        return if (response != null) {
            val entities = response.data.map { it.toEntity() }
            dao.insertAll(entities)
            entities.map { it.toDomain() }
        } else {
            emptyList()
        }
    }

    override suspend fun getPlaceById(id: String): Place {
        return getPlaces().first().firstOrNull { it.id == id } ?: Place()
    }

    suspend fun refreshCities(offset: Int) {
        try {
            val response = api.getCities(offset = offset)
            dao.insertAll(response.data.map { it.toEntity() })
        } catch (e: Exception) {
            // Log error
        }
    }
}
