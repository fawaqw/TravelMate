package com.example.travelmate.data.mapper

import com.example.travelmate.data.local.entity.CityEntity
import com.example.travelmate.data.remote.dto.CityDto
import com.example.travelmate.domain.model.Place

fun CityDto.toEntity() = CityEntity(
    id = id,
    name = name,
    country = country,
    population = population
)

fun CityEntity.toDomain() = Place(
    id = id.toString(),
    name = name,
    city = name,
    country = country,
    imageUrl = "https://source.unsplash.com/600x400/?$name",
    description = "Population: $population",
    rating = (population % 5 + 1).toDouble(),
    category = country
)
