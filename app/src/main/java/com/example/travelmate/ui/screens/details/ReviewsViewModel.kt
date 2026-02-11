package com.exaple.travelmate.ui.screens.details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.travelmate.data.remote.review.ReviewRemoteDataSource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class ReviewsViewModel @Inject constructor(
    private val remote: ReviewRemoteDataSource
) : ViewModel() {

    fun reviews(placeId: String) =
        remote.observeReviews(placeId)
            .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())
}
