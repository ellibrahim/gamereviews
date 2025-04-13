package com.example.gamereviews.navigation

import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState

// Define bottom navigation items
sealed class BottomNavItem(val route: String, val title: String, val icon: ImageVector) {
    object Games : BottomNavItem("games", "", Icons.Filled.Home)
    object RecentReviews : BottomNavItem("recent_reviews", "", Icons.Filled.Star)
    object UserReviews : BottomNavItem("user_reviews", "", Icons.Filled.Person)
    object Settings : BottomNavItem("settings", "", Icons.Filled.Settings)
}

// Bottom Navigation Bar UI
@Composable
fun BottomNavigationBar(navController: NavController) {
    val items = listOf(
        BottomNavItem.Games,
        BottomNavItem.RecentReviews,
        BottomNavItem.UserReviews,
        BottomNavItem.Settings
    )
    val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route

    NavigationBar {
        items.forEach { item ->
            NavigationBarItem(
                icon = { Icon(imageVector = item.icon, contentDescription = item.title) },
                label = { Text(item.title) },
                selected = currentRoute == item.route,
                onClick = {
                    navController.navigate(item.route) {
                        // Ensure no multiple copies of the same screen in the back stack
                        popUpTo(navController.graph.startDestinationId) { inclusive = false }
                        launchSingleTop = true
                    }
                }
            )
        }
    }
}
