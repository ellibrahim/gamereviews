package com.example.gamereviews

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.gamereviews.com.example.gamereviews.Game
import com.example.gamereviews.com.example.gamereviews.gameDetails
import com.example.gamereviews.ui.theme.NeonGreen
import com.example.gamereviews.ui.theme.NeonBlue

@Composable
fun GamesScreen(
    onNavigateToGameDetails: (String) -> Unit
) {
    // Convert the gameDetails map to a list of Game objects
    val gameList = gameDetails.values.toList()

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = Color(0xFF121212) // Dark Background
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(Color(0xFF121212)), // Dark Background
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(gameList) { game ->
                GameItem(game, onNavigateToGameDetails)
            }
        }
    }
}

@Composable
fun GameItem(game: Game, onNavigateToGameDetails: (String) -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(120.dp)
            .clickable { onNavigateToGameDetails(game.title) },
        elevation = CardDefaults.cardElevation(defaultElevation = 12.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF1A1A1A)) // Darker card background
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Game image (loaded from resources)
            Image(
                painter = painterResource(id = game.imageResId),
                contentDescription = game.title,
                modifier = Modifier
                    .size(100.dp)
                    .padding(8.dp)
            )
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(8.dp)
            ) {
                // Game title
                Text(
                    text = game.title,
                    fontSize = 18.sp,
                    color = Color(0xFF00FF00), // Neon green
                    fontFamily = FontFamily.SansSerif
                )
                // Game release date
                Text(
                    text = "Released: ${game.releaseDate}",
                    fontSize = 14.sp,
                    color = Color(0xFFBBBBBB), // Lighter gray for contrast
                    fontFamily = FontFamily.SansSerif
                )
                // Game developer
                Text(
                    text = game.developer,
                    fontSize = 14.sp,
                    color = Color(0xFFBBBBBB),
                    fontFamily = FontFamily.SansSerif
                )
            }
        }
    }
}
