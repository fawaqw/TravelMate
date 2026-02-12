package com.example.travelmate.di

import android.content.Context
import androidx.room.Room
import com.example.travelmate.data.local.AppDatabase
import com.example.travelmate.data.remote.GeoDbApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    fun provideOkHttp(): OkHttpClient =
        OkHttpClient.Builder()
            .addInterceptor { chain ->
                val request = chain.request().newBuilder()
                    .addHeader("X-RapidAPI-Key", "PUT_YOUR_KEY_HERE")
                    .addHeader("X-RapidAPI-Host", "wft-geo-db.p.rapidapi.com")
                    .build()
                chain.proceed(request)
            }
            .build()

    @Provides
    fun provideApi(client: OkHttpClient): GeoDbApi =
        Retrofit.Builder()
            .baseUrl("https://wft-geo-db.p.rapidapi.com/")
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(GeoDbApi::class.java)

    @Provides
    fun provideDb(@ApplicationContext ctx: Context) =
        Room.databaseBuilder(ctx, AppDatabase::class.java, "travel.db")
            .fallbackToDestructiveMigration()
            .build()

    @Provides
    fun provideCityDao(db: AppDatabase) = db.cityDao()

}
