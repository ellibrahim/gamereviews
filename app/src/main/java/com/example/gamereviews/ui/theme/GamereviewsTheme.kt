package com.example.gamereviews.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

val NeonGreen = Color(0xFF00FF66)
val NeonBlue = Color(0xFF0099FF)
val DarkBackground = Color(0xFF121212)
val CardBackground = Color(0xFF1E1E1E)

@Composable
fun GamereviewsTheme(content: @Composable () -> Unit) {
    val darkTheme = isSystemInDarkTheme()
    MaterialTheme(
        colorScheme = darkColorScheme(
            primary = NeonGreen,
            onPrimary = Color.Black,
            secondary = NeonBlue,
            onSecondary = Color.Black,
            background = DarkBackground,
            onBackground = Color.White,
            surface = CardBackground,
            onSurface = Color.White
        ),
        typography = Typography(),
        content = content
    )
}
