package com.example.travelmate.ui.screens.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.travelmate.data.remote.review.ReviewDto
import com.example.travelmate.data.remote.review.ReviewRemoteDataSource
import com.example.travelmate.domain.model.Place
import com.example.travelmate.domain.repository.PlaceRepository
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val repo: PlaceRepository,
    private val remote: ReviewRemoteDataSource
) : ViewModel() {

    private val auth = FirebaseAuth.getInstance()

    val userEmail: String
        get() = auth.currentUser?.email ?: "No email"

    val favoritePlaces: StateFlow<List<Place>> = repo.getPlaces()
        .map { places -> places.filter { it.isFavorite } }
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    val userReviews = remote.observeAllReviews()
        .map { all ->
            val userId = auth.currentUser?.uid
            all.filter { it.userId == userId }
        }
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    fun logout() {
        auth.signOut()
    }

    fun toggleFavorite(placeId: String) {
        viewModelScope.launch {
            repo.toggleFavorite(placeId)
        }
    }

    fun deleteReview(review: ReviewDto) {
        val now = System.currentTimeMillis()
        val oneDayMillis = 24 * 60 * 60 * 1000
        if (now - review.timestamp <= oneDayMillis) {
            viewModelScope.launch {
                remote.deleteReview(review.placeId, review.id)
            }
        }
    }
}
