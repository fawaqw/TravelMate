package com.example.travelmate.ui.screens.review

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.travelmate.data.remote.review.ReviewDto
import com.example.travelmate.data.remote.review.ReviewRemoteDataSource
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class CreateReviewViewModel @Inject constructor(
    private val remote: ReviewRemoteDataSource
) : ViewModel() {

    private val auth = FirebaseAuth.getInstance()
    private val _placeName = MutableStateFlow("")
    val placeName = _placeName.asStateFlow()

    fun loadPlaceName(placeId: String) {
        viewModelScope.launch {
            try {
                val placeSnapshot = FirebaseDatabase.getInstance().getReference("places").child(placeId).get().await()
                _placeName.value = placeSnapshot.child("name").getValue(String::class.java) ?: "Unknown Place"
            } catch (e: Exception) {
                _placeName.value = "Unknown Place"
            }
        }
    }

    fun submit(placeId: String, text: String, rating: Int, userId: String) {
        if (text.isBlank() || rating == 0) return

        viewModelScope.launch {
            try {
                val review = ReviewDto(
                    id = UUID.randomUUID().toString(),
                    userId = userId,
                    userName = auth.currentUser?.displayName ?: "Traveler",
                    placeId = placeId,
                    placeName = _placeName.value,
                    text = text,
                    rating = rating,
                    timestamp = System.currentTimeMillis()
                )
                remote.addReview(review)
            } catch (e: Exception) {
                // Handle error
            }
        }
    }
}
