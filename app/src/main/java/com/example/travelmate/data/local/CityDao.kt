package com.example.travelmate.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.travelmate.data.local.entity.CityEntity
import kotlinx.coroutines.flow.Flow


@Dao
interface CityDao {

    @Query("SELECT * FROM cities")
    fun getCities(): Flow<List<CityEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(cities: List<CityEntity>)
}
