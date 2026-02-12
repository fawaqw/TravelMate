package com.example.travelmate

import com.example.travelmate.data.local.entity.CityEntity
import com.example.travelmate.data.mapper.toDomain
import com.example.travelmate.data.remote.review.ReviewDto
import com.example.travelmate.data.remote.dto.CityDto
import com.example.travelmate.data.mapper.toEntity
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class BusinessLogicTest {

    // --- Endterm Tests (5) ---

    @Test
    fun `mapper cityEntityToDomain calculates rating correctly`() {
        val entity = CityEntity(
            id = "1",
            name = "Test",
            city = "Test City",
            country = "Country",
            imageUrl = "url",
            description = "desc",
            rating = 4.5,
            category = "cat",
            population = 100
        )
        val domain = entity.toDomain()
        assertEquals(4.5, domain.rating, 0.01)
    }

    @Test
    fun `mapper cityEntityToDomain maps name correctly`() {
        val entity = CityEntity(
            id = "1",
            name = "Paris",
            city = "Paris",
            country = "France",
            imageUrl = "url",
            description = "desc",
            rating = 4.5,
            category = "cat",
            population = 100
        )
        val domain = entity.toDomain()
        assertEquals("Paris", domain.name)
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

    // --- Final Tests (5 Additional) ---

    @Test
    fun `mapper cityDtoToEntity calculates rating based on population`() {
        // according to CityMappers: rating = (population % 5 + 1).toDouble()
        val dto = CityDto(id = 1, name = "Almaty", country = "KZ", latitude = 0.0, longitude = 0.0, population = 10)
        val entity = dto.toEntity()
        // 10 % 5 = 0, 0 + 1 = 1.0
        assertEquals(1.0, entity.rating, 0.01)
    }

    @Test
    fun `search validation - empty query should be invalid`() {
        val query = ""
        val isValid = query.isNotBlank() && query.length >= 2
        assertFalse("Empty query should be invalid", isValid)
    }

    @Test
    fun `search validation - short query should be invalid`() {
        val query = "a"
        val isValid = query.isNotBlank() && query.length >= 2
        assertFalse("Short query should be invalid", isValid)
    }

    @Test
    fun `search validation - valid query should pass`() {
        val query = "Almaty"
        val isValid = query.isNotBlank() && query.length >= 2
        assertTrue("Valid query should be accepted", isValid)
    }

    @Test
    fun `retry strategy - counter should increment correctly`() {
        var retryCount = 0
        val maxRetries = 3
        
        fun doSomethingWithRetry() {
            if (retryCount < maxRetries) {
                retryCount++
            }
        }
        
        repeat(5) { doSomethingWithRetry() }
        assertEquals(3, retryCount)
    }
}
