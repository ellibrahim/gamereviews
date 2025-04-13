package com.example.gamereviews

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.gamereviews.com.example.gamereviews.gameDetails
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyReviewsScreen(navController: NavController) {
    val db = FirebaseFirestore.getInstance()
    val currentUserId = FirebaseAuth.getInstance().currentUser?.uid
    val reviews = remember { mutableStateListOf<ReviewWithGameDetails>() }
    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        if (currentUserId != null) {
            db.collection("all_reviews")
                .whereEqualTo("userId", currentUserId)
                .orderBy("timestamp", Query.Direction.DESCENDING)
                .addSnapshotListener { snapshot, _ ->
                    if (snapshot != null) {
                        reviews.clear()
                        for (doc in snapshot.documents) {
                            val review = doc.toObject(Review::class.java)?.copy(id = doc.id)
                            val gameTitle = review?.gameTitle
                            if (review != null && gameTitle != null) {
                                val gameDetails = gameDetails[gameTitle]
                                db.collection("users")
                                    .document(review.userId)
                                    .get()
                                    .addOnSuccessListener { userSnapshot ->
                                        val username = userSnapshot.getString("username") ?: "Anonymous"
                                        val reviewWithDetails = ReviewWithGameDetails(
                                            review = review.copy(username = username),
                                            gameTitle = gameDetails?.title ?: "Unknown Game",
                                            gameImageResId = gameDetails?.imageResId ?: 0
                                        )
                                        reviews.add(reviewWithDetails)
                                    }
                            }
                        }
                    }
                }
        }
    }

    val onEditReview: (ReviewWithGameDetails) -> Unit = { reviewWithDetails ->
        navController.navigate("editReview/${reviewWithDetails.review.id}")
    }

    val onDeleteReview: (ReviewWithGameDetails) -> Unit = { reviewWithDetails ->
        db.collection("all_reviews")
            .document(reviewWithDetails.review.id)
            .delete()
            .addOnSuccessListener {
                coroutineScope.launch {
                    snackbarHostState.showSnackbar("Review deleted successfully")
                }
            }
            .addOnFailureListener {
                coroutineScope.launch {
                    snackbarHostState.showSnackbar("Failed to delete review")
                }
            }
    }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("My Reviews") })
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
                reviews.forEach { reviewWithDetails ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp)
                            .height(120.dp),
                        elevation = CardDefaults.cardElevation(4.dp),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFF1A1A1A))
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(8.dp)
                        ) {
                            Image(
                                painter = painterResource(id = reviewWithDetails.gameImageResId),
                                contentDescription = "Game Image",
                                modifier = Modifier
                                    .width(80.dp)
                                    .fillMaxHeight()
                            )

                            Spacer(modifier = Modifier.width(8.dp))

                            Column(
                                modifier = Modifier
                                    .fillMaxHeight()
                                    .weight(1f)
                                    .padding(8.dp)
                            ) {
                                Text(
                                    text = reviewWithDetails.gameTitle,
                                    fontSize = 16.sp,
                                    color = Color(0xFF00FF00),
                                    fontWeight = FontWeight.Bold
                                )

                                Text(
                                    text = reviewWithDetails.review.username,
                                    fontSize = 14.sp,
                                    color = Color(0xFFBBBBBB)
                                )

                                Spacer(modifier = Modifier.height(4.dp))

                                Text(
                                    text = reviewWithDetails.review.reviewText,
                                    fontSize = 12.sp,
                                    color = Color(0xFFBBBBBB),
                                    maxLines = 2,
                                    overflow = TextOverflow.Ellipsis
                                )
                            }
                        }
                    }

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 8.dp, vertical = 4.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Button(
                            onClick = { onEditReview(reviewWithDetails) },
                            modifier = Modifier.weight(1f)
                        ) {
                            Text("Edit")
                        }

                        Spacer(modifier = Modifier.width(8.dp))

                        Button(
                            onClick = { onDeleteReview(reviewWithDetails) },
                            modifier = Modifier.weight(1f),
                            colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
                        ) {
                            Text("Delete", color = Color.White)
                        }
                    }
                }
            }
        }
    }
}
