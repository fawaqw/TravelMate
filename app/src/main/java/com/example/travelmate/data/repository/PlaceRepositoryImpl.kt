package com.example.travelmate.data.repository

import com.example.travelmate.data.local.CityDao
import com.example.travelmate.data.mapper.toEntity
import com.example.travelmate.data.remote.GeoDbApi
import com.example.travelmate.domain.model.Place
import com.example.travelmate.domain.repository.PlaceRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject




class PlaceRepositoryImpl @Inject constructor(
    private val api: GeoDbApi,
    private val dao: CityDao
) : PlaceRepository {

    override fun getPlaces(): Flow<List<Place>> =
        dao.getCities().map { list ->
            list.map { it.toDomain() }
        }

    override suspend fun searchPlaces(query: String): List<Place> {
        val response = api.searchCities(query)
        val entities = response.data.map { it.toEntity() }
        dao.insertAll(entities)
        return entities.map { it.toDomain() }
    }

    override suspend fun getPlaceById(id: String): Place {
        return dao.getCities()
            .first()
            .first { it.id.toString() == id }
            .toDomain()
    }

    suspend fun refreshCities(offset: Int = 0) {
        val response = api.getCities(offset = offset)
        val entities = response.data.map { it.toEntity() }
        dao.insertAll(entities)
    }
}
