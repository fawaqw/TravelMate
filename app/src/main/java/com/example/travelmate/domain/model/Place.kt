package com.example.travelmate.domain.model

data class Place(
    val id: String = "",
    val name: String = "",
    val city: String = "",
    val country: String = "",
    val imageUrl: String = "",
    val description: String = "",
    val rating: Double = 0.0,
    val category: String = "",
    val isFavorite: Boolean = false
)
