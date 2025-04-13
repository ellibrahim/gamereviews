package com.example.gamereviews

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)

@Composable
fun EditReviewScreen(navController: NavController, reviewId: String) {
    // Fetch the review data based on the reviewId
    val db = FirebaseFirestore.getInstance()
    var reviewText by remember { mutableStateOf("") }
    var isSaving by remember { mutableStateOf(false) }

    // Fetch the review from Firestore when the reviewId changes
    LaunchedEffect(reviewId) {
        db.collection("all_reviews").document(reviewId).get()
            .addOnSuccessListener { document ->
                if (document.exists()) {
                    reviewText = document.getString("reviewText") ?: ""
                }
            }
    }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Edit Review") })
        }
    ) { paddingValues ->
        Column(modifier = Modifier.padding(paddingValues).padding(16.dp)) {
            Text("Edit your review:", style = MaterialTheme.typography.labelMedium)
            Spacer(modifier = Modifier.height(16.dp))

            // TextField to edit review
            TextField(
                value = reviewText,
                onValueChange = { reviewText = it },
                label = { Text("Review Text") },
                modifier = Modifier.fillMaxWidth(),
                maxLines = 5
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    // Save the updated review to Firestore
                    if (reviewText.isNotBlank()) {
                        isSaving = true
                        db.collection("all_reviews")
                            .document(reviewId)
                            .update("reviewText", reviewText)
                            .addOnSuccessListener {
                                // After saving, navigate back to My Reviews
                                isSaving = false
                                navController.navigate("userReviews") {
                                    popUpTo("userReviews") { inclusive = true }
                                }
                            }
                            .addOnFailureListener {
                                isSaving = false
                                // Handle failure
                            }
                    }
                },
                enabled = !isSaving,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Save Review")
            }

            if (isSaving) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
            }
        }
    }
}

