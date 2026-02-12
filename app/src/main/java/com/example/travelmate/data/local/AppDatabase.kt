package com.example.travelmate.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.travelmate.data.local.entity.CityEntity

@Database(entities = [CityEntity::class], version = 2)
abstract class AppDatabase : RoomDatabase() {
    abstract fun cityDao(): CityDao
}
