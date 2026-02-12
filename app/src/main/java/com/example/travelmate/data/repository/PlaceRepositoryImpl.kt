package com.example.travelmate.data.repository

import android.util.Log
import com.example.travelmate.data.local.CityDao
import com.example.travelmate.data.mapper.toDomain
import com.example.travelmate.data.mapper.toEntity
import com.example.travelmate.data.remote.GeoDbApi
import com.example.travelmate.domain.model.Place
import com.example.travelmate.domain.repository.PlaceRepository
import com.example.travelmate.data.remote.review.ReviewRemoteDataSource
import com.google.firebase.auth.FirebaseAuth
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
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class PlaceRepositoryImpl @Inject constructor(
    private val api: GeoDbApi,
    private val dao: CityDao,
    private val reviewDataSource: ReviewRemoteDataSource
) : PlaceRepository {

    private val database = FirebaseDatabase.getInstance().getReference("places")
    private val auth = FirebaseAuth.getInstance()

    private fun getFavoritesRef() = FirebaseDatabase.getInstance()
        .getReference("users")
        .child(auth.currentUser?.uid ?: "anonymous")
        .child("favorites")

    override fun getPlaces(): Flow<List<Place>> {
        val favoritesFlow = callbackFlow<Set<String>> {
            val ref = getFavoritesRef()
            val listener = object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val favs = snapshot.children.mapNotNull { it.key }.toSet()
                    trySend(favs)
                }
                override fun onCancelled(error: DatabaseError) { close(error.toException()) }
            }
            ref.addValueEventListener(listener)
            awaitClose { ref.removeEventListener(listener) }
        }

        val firebaseFlow = callbackFlow {
            val listener = object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val items = snapshot.children.mapNotNull { it.getValue(Place::class.java) }
                    trySend(items)
                }
                override fun onCancelled(error: DatabaseError) { close(error.toException()) }
            }
            database.addValueEventListener(listener)
            awaitClose { database.removeEventListener(listener) }
        }.onEach { places ->
            dao.insertAll(places.map { it.toEntity() })
        }

        return combine(
            firebaseFlow, 
            dao.getCities().map { list -> list.map { it.toDomain() } },
            reviewDataSource.observeAllReviews(),
            favoritesFlow
        ) { remote, local, reviews, favorites ->
            val source = if (remote.isNotEmpty()) remote else local
            source.map { place ->
                val placeReviews = reviews.filter { it.placeId == place.id }
                val avgRating = if (placeReviews.isNotEmpty()) {
                    placeReviews.map { it.rating }.average()
                } else {
                    place.rating
                }
                place.copy(
                    rating = Math.round(avgRating * 10.0) / 10.0,
                    isFavorite = favorites.contains(place.id)
                )
            }
        }
    }

    override suspend fun toggleFavorite(placeId: String) {
        val ref = getFavoritesRef().child(placeId)
        val snapshot = ref.get().await()
        if (snapshot.exists()) {
            ref.removeValue()
        } else {
            ref.setValue(true)
        }
    }

    override suspend fun searchPlaces(query: String): List<Place> {
        return getPlaces().first().filter { 
            it.name.contains(query, ignoreCase = true) || 
            it.city.contains(query, ignoreCase = true) ||
            it.country.contains(query, ignoreCase = true)
        }
    }

    override suspend fun getPlaceById(id: String): Place {
        return getPlaces().first().firstOrNull { it.id == id } ?: Place()
    }

    override suspend fun refreshCities(offset: Int, limit: Int) {
        try {
            val response = api.getCities(offset = offset, limit = limit)
            dao.insertAll(response.data.map { it.toEntity() })
        } catch (e: Exception) {
            Log.e("PlaceRepository", "Error refreshing cities", e)
        }
    }
}
