// RecentReviewsScreen.kt or wherever you need to use the gameDetails map
package com.example.gamereviews

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.font.FontWeight
import androidx.navigation.NavController
import com.example.gamereviews.com.example.gamereviews.gameDetails
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.launch

// Import the gameDetails map
//import com.example.gamereviews.

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecentReviewsScreen(navController: NavController) {
    val db = FirebaseFirestore.getInstance()
    val reviews = remember { mutableStateListOf<ReviewWithGameDetails>() }
    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        // Fetch recent reviews from the all_reviews collection
        db.collection("all_reviews")
            .orderBy("timestamp", Query.Direction.DESCENDING)
            .limit(100) // Limit to 100 most recent reviews
            .addSnapshotListener { snapshot, _ ->
                if (snapshot != null) {
                    reviews.clear()
                    for (doc in snapshot.documents) {
                        val review = doc.toObject(Review::class.java)
                        val gameTitle = review?.gameTitle
                        if (review != null && gameTitle != null) {
                            // Get game details using the title
                            val gameDetails = gameDetails[gameTitle]
                            if (gameDetails != null) {
                                // Fetch tٍhe username from the users collection
                                db.collection("users")
                                    .document(review.userId)
                                    .get()
                                    .addOnSuccessListener { userSnapshot ->
                                        val username = userSnapshot.getString("username") ?: "Anonymous"

                                        // Combine review and game details with username
                                        val reviewWithDetails = ReviewWithGameDetails(
                                            review = review.copy(username = username),
                                            gameTitle = gameDetails.title,
                                            gameImageResId = gameDetails.imageResId // Get image from resources
                                        )
                                        reviews.add(reviewWithDetails)
                                    }
                            }
                        }
                    }
                }
            }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Recent Reviews") }
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
        ) {
            if (reviews.isEmpty()) {
                Text("No reviews available.", modifier = Modifier.padding(16.dp))
            } else {
                reviews.forEach { reviewWithGameDetails ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp)
                            .heightIn(min = 100.dp), // Smaller height for the card
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp),
                            verticalAlignment = Alignment.CenterVertically // Align vertically in the center
                        ) {
                            // Game image (loaded from resources)
                            Image(
                                painter = painterResource(id = reviewWithGameDetails.gameImageResId),
                                contentDescription = reviewWithGameDetails.gameTitle,
                                modifier = Modifier
                                    .size(80.dp) // Adjust image size
                                    .padding(end = 16.dp) // Space between image and text
                            )

                            // Text content (game title, username, review, and rating)
                            Column(
                                modifier = Modifier.weight(1f) // Take up remaining space
                            ) {
                                // Game title
                                Text(
                                    text = reviewWithGameDetails.gameTitle,
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(bottom = 4.dp)
                                )

                                // Display the username
                                Text(
                                    text = reviewWithGameDetails.review.username,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Medium,
                                    modifier = Modifier.padding(bottom = 4.dp)
                                )

                                // Review content
                                Text(
                                    text = reviewWithGameDetails.review.reviewText ?: "No review text.",
                                    fontSize = 14.sp,
                                    modifier = Modifier.padding(bottom = 4.dp)
                                )

                                // Rating
                                Row {
                                    (1..5).forEach { star ->
                                        Icon(
                                            imageVector = if (star <= reviewWithGameDetails.review.rating) Icons.Filled.Star else Icons.Outlined.Star,
                                            contentDescription = "Star Rating",
                                            tint = MaterialTheme.colorScheme.primary
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

data class ReviewWithGameDetails(
    val review: Review,
    val gameTitle: String,
    val gameImageResId: Int
)