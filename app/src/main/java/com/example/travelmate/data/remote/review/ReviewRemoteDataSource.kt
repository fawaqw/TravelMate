package com.example.travelmate.data.remote.review

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

class ReviewRemoteDataSource {

    private val db = FirebaseDatabase.getInstance().reference

    fun observeReviews(placeId: String): Flow<List<ReviewDto>> = callbackFlow {
        val ref = db.child("reviews").child(placeId)

        val listener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val list = snapshot.children.mapNotNull {
                    it.getValue(ReviewDto::class.java)
                }
                trySend(list)
            }

            override fun onCancelled(error: DatabaseError) {}
        }

        ref.addValueEventListener(listener)
        awaitClose { ref.removeEventListener(listener) }
    }

    fun observeAllReviews(): Flow<List<ReviewDto>> = callbackFlow {
        val ref = db.child("reviews")
        val listener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val list = mutableListOf<ReviewDto>()
                snapshot.children.forEach { placeSnap ->
                    placeSnap.children.forEach { reviewSnap ->
                        reviewSnap.getValue(ReviewDto::class.java)?.let { list.add(it) }
                    }
                }
                trySend(list)
            }
            override fun onCancelled(error: DatabaseError) {}
        }
        ref.addValueEventListener(listener)
        awaitClose { ref.removeEventListener(listener) }
    }

    suspend fun addReview(review: ReviewDto) {
        db.child("reviews")
            .child(review.placeId)
            .child(review.id)
            .setValue(review)
    }

    suspend fun deleteReview(placeId: String, reviewId: String) {
        db.child("reviews").child(placeId).child(reviewId).removeValue()
    }
}
