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
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

class PlaceRepositoryImpl @Inject constructor(
    private val api: GeoDbApi,
    private val dao: CityDao,
    private val reviewDataSource: ReviewRemoteDataSource
) : PlaceRepository {

    private val database = FirebaseDatabase.getInstance().getReference("places")

    override fun getPlaces(): Flow<List<Place>> {
        // source from Firebase, but also mirror to Room for offline access
        val firebaseFlow = callbackFlow {
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
        }.onEach { places ->
            // save to local DB whenever we get updates from Firebase
            dao.insertAll(places.map { it.toEntity() })
        }

        // the UI will observe this flow. If online, it gets Firebase data (mirrored to Room).
        // if offline, firebaseFlow will fail/stay empty, so we combine with local Room data.
        return combine(
            firebaseFlow, 
            dao.getCities().map { list -> list.map { it.toDomain() } },
            reviewDataSource.observeAllReviews()
        ) { remote, local, reviews ->
            val source = if (remote.isNotEmpty()) remote else local
            source.map { place ->
                val placeReviews = reviews.filter { it.placeId == place.id }
                val avgRating = if (placeReviews.isNotEmpty()) {
                    placeReviews.map { it.rating }.average()
                } else {
                    place.rating
                }
                place.copy(rating = Math.round(avgRating * 10.0) / 10.0)
            }
        }
    }

    override suspend fun searchPlaces(query: String): List<Place> {
        // try searching in the current flow (which handles both remote and local)
        return getPlaces().first().filter { 
            it.name.contains(query, ignoreCase = true) || 
            it.city.contains(query, ignoreCase = true) ||
            it.country.contains(query, ignoreCase = true)
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
            // handle error - maybe stay offline
        }
    }
}
