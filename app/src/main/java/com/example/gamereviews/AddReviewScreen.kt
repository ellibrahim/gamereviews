
package com.example.gamereviews

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FieldValue
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddReviewScreen(gameTitle: String, navController: NavController) {
    var reviewText by remember { mutableStateOf("") }
    var rating by remember { mutableStateOf(0) }
    val userId = FirebaseAuth.getInstance().currentUser?.uid
    val db = FirebaseFirestore.getInstance()
    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()
    var isSubmitting by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Add Review") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Add a Review for $gameTitle", fontSize = 20.sp)

            Spacer(modifier = Modifier.height(16.dp))

            // Star Rating Selector
            Row {
                (1..5).forEach { star ->
                    IconButton(onClick = { rating = star }) {
                        Icon(
                            imageVector = if (star <= rating) Icons.Filled.Star else Icons.Outlined.Star,
                            contentDescription = "Star Rating",
                            tint = if (star <= rating) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Review Text Input
            OutlinedTextField(
                value = reviewText,
                onValueChange = { reviewText = it },
                label = { Text("Write your review") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Submit Button
            Button(
                onClick = {
                    if (userId == null) {
                        coroutineScope.launch {
                            snackbarHostState.showSnackbar("You must be logged in to submit a review.")
                        }
                        return@Button
                    }
                    if (rating == 0) {
                        coroutineScope.launch {
                            snackbarHostState.showSnackbar("Please select a rating before submitting.")
                        }
                        return@Button
                    }
                    if (reviewText.isBlank()) {
                        coroutineScope.launch {
                            snackbarHostState.showSnackbar("Please write a review before submitting.")
                        }
                        return@Button
                    }

                    isSubmitting = true

                    // Create a Review data map including gameTitle
                    val reviewData = hashMapOf(
                        "userId" to userId,
                        "reviewText" to reviewText,
                        "rating" to rating.toDouble(),
                        "gameTitle" to gameTitle,   // Add gameTitle to the review
                        "timestamp" to FieldValue.serverTimestamp()
                    )

                    // Save the review to the Firestore subcollection for the specific game
                    db.collection("games").document(gameTitle)
                        .collection("reviews")
                        .add(reviewData)
                        .addOnSuccessListener {
                            isSubmitting = false
                            coroutineScope.launch {
                                snackbarHostState.showSnackbar("Review submitted successfully!")
                            }
                            navController.popBackStack()
                        }
                        .addOnFailureListener {
                            isSubmitting = false
                            coroutineScope.launch {
                                snackbarHostState.showSnackbar("Failed to submit review. Try again.")
                            }
                        }


                    db.collection("all_reviews")
                        .add(reviewData)
                        .addOnSuccessListener {

                        }
                        .addOnFailureListener {

                        }
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = !isSubmitting
            ) {
                if (isSubmitting) {
                    CircularProgressIndicator(modifier = Modifier.size(24.dp))
                } else {
                    Text("Submit Review")
                }
            }
        }
    }
}
