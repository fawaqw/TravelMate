package com.example.travelmate.domain.model

data class Place(
    val id: String,
    val name: String,
    val imageUrl: String,
    val description: String,
    val rating: Double,
    val category: String
)
