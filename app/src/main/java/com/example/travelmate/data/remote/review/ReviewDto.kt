package com.example.travelmate.data.remote.review

data class ReviewDto(
    val id: String = "",
    val userId: String = "",
    val userName: String = "",
    val placeId: String = "",
    val placeName: String = "",
    val text: String = "",
    val rating: Int = 0,
    val timestamp: Long = System.currentTimeMillis()
)
