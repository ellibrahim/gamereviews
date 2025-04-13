package com.example.gamereviews
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.firebase.Timestamp


    data class Review(
    val username: String = "Anonymous",  // Add default value for username
    val userId: String = "", // Add userId field (with a default value)
    val id: String = "",  // Add this line
    val gameTitle: String = "",
    val reviewText: String = "",
    val rating: Int = 0,
    val timestamp: Timestamp? = null
)

data class ReviewWithId(
    val id: String,
    val review: Review
)


@Composable
fun ReviewItem(reviewWithId: ReviewWithId) {
    val review = reviewWithId.review  // Accessing the Review object inside ReviewWithId

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .shadow(8.dp),
        elevation = CardDefaults.cardElevation(4.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF1A1A1A)) // Dark background for reviews
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Display username and review content
            Text(
                text = review.username,
                fontSize = 16.sp,
                color = Color(0xFF00FF00), // Neon green color for username
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(4.dp))

            // Display the rating
            Text(
                text = "⭐ ${review.rating}/5",  // Show the rating
                fontSize = 16.sp,
                color = Color(0xFF00FF00),  // Neon green color for the rating
            )
            Spacer(modifier = Modifier.height(4.dp))

            // Display the review text
            Text(
                text = review.reviewText,
                fontSize = 14.sp,
                color = Color(0xFFBBBBBB), // Light gray for review content
            )
            Spacer(modifier = Modifier.height(4.dp))

            // Display the timestamp
            review.timestamp?.let {
                Text(
                    text = "Reviewed on: ${it.toDate().toString()}", // Convert timestamp to date
                    fontSize = 12.sp,
                    color = Color(0xFFBBBBBB)  // Light gray for timestamp
                )
            }
        }
    }
}

