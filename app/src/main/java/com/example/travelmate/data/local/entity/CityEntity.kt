package com.example.travelmate.data.local.entity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "cities",
    indices = [Index(value = ["name"]), Index(value = ["country"])]
)
data class CityEntity(
    @PrimaryKey val id: String,
    val name: String,
    val city: String,
    val country: String,
    val imageUrl: String,
    val description: String,
    val rating: Double,
    val category: String,
    val population: Int = 0
)
