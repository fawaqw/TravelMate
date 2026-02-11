package com.exaple.travelmate.di

import com.example.travelmate.data.remote.review.ReviewRemoteDataSource
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object FirebaseModule {

    @Provides
    fun provideReviewRemote() = ReviewRemoteDataSource()
}
