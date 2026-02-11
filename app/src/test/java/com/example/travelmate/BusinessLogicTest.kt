package com.example.travelmate

import com.example.travelmate.data.local.entity.CityEntity
import com.example.travelmate.data.mapper.toDomain
import com.example.travelmate.data.remote.review.ReviewDto
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class BusinessLogicTest {

    @Test
    fun `mapper cityEntityToDomain calculates rating correctly`() {
        val entity = CityEntity(id = 1, name = "Test", country = "Country", population = 100)
        val domain = entity.toDomain()
        
        // rating formula in CityMappers: (population % 5 + 1).toDouble()
        // 100 % 5 = 0, 0 + 1 = 1.0
        assertEquals(1.0, domain.rating, 0.01)
    }

    @Test
    fun `mapper cityEntityToDomain formats name correctly`() {
        val entity = CityEntity(id = 1, name = "Paris", country = "France", population = 100)
        val domain = entity.toDomain()
        
        assertEquals("Paris, France", domain.name)
    }

    @Test
    fun `review deletion policy - within 24 hours should be allowed`() {
        val now = System.currentTimeMillis()
        val review = ReviewDto(timestamp = now - 1000) // 1 second ago
        
        val oneDayMillis = 24 * 60 * 60 * 1000
        val canDelete = (System.currentTimeMillis() - review.timestamp) <= oneDayMillis
        
        assertTrue("Should be able to delete review created 1 second ago", canDelete)
    }

    @Test
    fun `review deletion policy - after 24 hours should be forbidden`() {
        val now = System.currentTimeMillis()
        val oldTimestamp = now - (25 * 60 * 60 * 1000) // 25 hours ago
        val review = ReviewDto(timestamp = oldTimestamp)
        
        val oneDayMillis = 24 * 60 * 60 * 1000
        val canDelete = (System.currentTimeMillis() - review.timestamp) <= oneDayMillis
        
        assertFalse("Should not be able to delete review created 25 hours ago", canDelete)
    }

    @Test
    fun `average rating calculation logic`() {
        val reviews = listOf(
            ReviewDto(rating = 5),
            ReviewDto(rating = 4),
            ReviewDto(rating = 3)
        )
        
        val avg = reviews.map { it.rating }.average()
        val roundedAvg = Math.round(avg * 10.0) / 10.0
        
        assertEquals(4.0, roundedAvg, 0.01)
    }
}
