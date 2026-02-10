package com.example.travelmate.domain.model

data class Review(
    val id: String,
    val userId: String,
    val placeId: String,
    val text: String,
    val rating: Int
)
