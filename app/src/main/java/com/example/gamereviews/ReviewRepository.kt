package com.example.gamereviews.com.example.gamereviews

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.auth.FirebaseAuth
import java.util.UUID

fun addReview(gameId: String, rating: Int, reviewText: String, onSuccess: () -> Unit, onFailure: (Exception) -> Unit) {
    val db = FirebaseFirestore.getInstance()
    val user = FirebaseAuth.getInstance().currentUser ?: return

    val review = hashMapOf(
        "userId" to user.uid,
        "username" to user.displayName,  // Make sure users have a display name set
        "rating" to rating,
        "reviewText" to reviewText,
        "timestamp" to System.currentTimeMillis()
    )

    db.collection("games").document(gameId)
        .collection("reviews").document(UUID.randomUUID().toString())
        .set(review)
        .addOnSuccessListener { onSuccess() }
        .addOnFailureListener { onFailure(it) }
}
