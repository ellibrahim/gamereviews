package com.example.gamereviews

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.example.gamereviews.com.example.gamereviews.gameDetails

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GameDetailsScreen(gameTitle: String, navController: NavController) {
    val game = gameDetails[gameTitle]  // Assuming you have a gameDetails map or similar for game data
    val reviews = remember { mutableStateListOf<ReviewWithId>() }

    // Fetch reviews from Firestore
    LaunchedEffect(gameTitle) {
        FirebaseFirestore.getInstance()
            .collection("games").document(gameTitle)
            .collection("reviews")
            .orderBy("timestamp", Query.Direction.DESCENDING)
            .addSnapshotListener { snapshot, _ ->
                if (snapshot != null) {
                    reviews.clear()
                    for (doc in snapshot.documents) {
                        val review = doc.toObject(Review::class.java)
                        if (review != null) {
                            // Fetch the username from the users collection based on userId
                            FirebaseFirestore.getInstance().collection("users")
                                .document(review.userId) // The userId of the reviewer
                                .get()
                                .addOnSuccessListener { userSnapshot ->
                                    val username = userSnapshot.getString("username") ?: "Anonymous"
                                    val reviewWithId = ReviewWithId(doc.id, review.copy(username = username))
                                    reviews.add(reviewWithId)
                                }
                        }
                    }
                }
            }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(gameTitle) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { navController.navigate("addReview/$gameTitle") }
            ) {
                Icon(Icons.Filled.Add, contentDescription = "Add Review")
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            game?.let {
                Image(
                    painter = painterResource(id = it.imageResId),
                    contentDescription = it.title,
                    modifier = Modifier.size(200.dp)
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = it.title, fontSize = 24.sp)
                Text(text = "Release Date: ${it.releaseDate}")
                Text(text = "Developer: ${it.developer}")
                Text(text = "Publisher: ${it.publisher}")
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = it.description)
                Spacer(modifier = Modifier.height(16.dp))
                Text(text = "Recent Reviews:")

                if (reviews.isEmpty()) {
                    Text("No reviews yet. Be the first to review!", fontSize = 14.sp)
                } else {
                    LazyColumn {
                        items(reviews) { reviewWithId ->
                            ReviewItem(reviewWithId = reviewWithId)
                        }
                    }
                }
            } ?: Text(text = "Game not found", fontSize = 20.sp)
        }
    }
}
