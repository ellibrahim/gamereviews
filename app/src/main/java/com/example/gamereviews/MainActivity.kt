package com.example.gamereviews
import com.example.gamereviews.navigation.BottomNavigationBar
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Settings
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.*
import com.example.gamereviews.ui.theme.GamereviewsTheme
import com.google.firebase.auth.FirebaseAuth
import androidx.navigation.NavType
import androidx.navigation.navArgument
import androidx.compose.material3.Scaffold
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.Text
import androidx.compose.ui.res.painterResource

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            GamereviewsTheme {
                val navController = rememberNavController()

                // Determine start destination based on login status
                val firebaseAuth = FirebaseAuth.getInstance()
                val startDestination = if (firebaseAuth.currentUser != null) "games" else "signIn"

                AppNavigation(navController, startDestination)
            }
        }
    }
    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun CustomTopBar() {
        CenterAlignedTopAppBar(
            title = {
                Image(
                    painter = painterResource(id = R.drawable.logo), // Make sure logo.png is in res/drawable
                    contentDescription = "App Logo",
                    modifier = Modifier
                        .height(40.dp)
                )
            },
            colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                containerColor = MaterialTheme.colorScheme.primary,
                titleContentColor = MaterialTheme.colorScheme.onPrimary
            ),
            modifier = Modifier.fillMaxWidth()
        )
    }
    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun AppNavigation(navController: NavHostController, startDestination: String) {

        Scaffold(
            topBar = { CustomTopBar() },
            bottomBar = { BottomNavBar(navController) }
        )  { paddingValues ->
            NavHost(
                navController = navController,
                startDestination = startDestination,
                modifier = Modifier.padding(paddingValues)
            ) {
                composable("signIn") {
                    SignInScreen(
                        onSignInSuccess = { navController.navigate("games") },
                        onNavigateToSignUp = { navController.navigate("signUp") }
                    )
                }

                composable("signUp") {
                    SignUpScreen(onSignUpSuccess = { navController.navigate("games") })
                }

                composable("games") {
                    GamesScreen(
                        onNavigateToGameDetails = { gameTitle -> navController.navigate("gameDetails/$gameTitle") }
                    )
                }

                composable("gameDetails/{gameTitle}") { backStackEntry ->
                    val gameTitle = backStackEntry.arguments?.getString("gameTitle") ?: "Unknown Game"
                    GameDetailsScreen(gameTitle = gameTitle, navController = navController)
                }

                composable("addReview/{gameTitle}") { backStackEntry ->
                    val gameTitle = backStackEntry.arguments?.getString("gameTitle") ?: "Unknown Game"
                    AddReviewScreen(gameTitle = gameTitle, navController = navController)
                }
                composable(
                    route = "editReview/{reviewId}",
                    arguments = listOf(navArgument("reviewId") { type = NavType.StringType })
                ) { backStackEntry ->
                    val reviewId = backStackEntry.arguments?.getString("reviewId") ?: return@composable
                    EditReviewScreen(navController, reviewId)
                }



                composable("recentReviews") { RecentReviewsScreen(navController) }

                composable("userReviews") { MyReviewsScreen(navController) }

                composable("settings") { SettingsScreen(navController) }
            }
        }
    }

    @Composable
    fun BottomNavBar(navController: NavHostController) {
        BottomNavigation(
            modifier = Modifier.fillMaxWidth(),
            backgroundColor = MaterialTheme.colorScheme.primary,  // You can adjust the background color as needed
            contentColor = MaterialTheme.colorScheme.onPrimary
        ) {
            BottomNavigationItem(
                selected = currentRoute(navController) == "games",
                onClick = { navController.navigate("games") },
                icon = {
                    Icon(
                        Icons.Filled.Home,
                        contentDescription = "Games",
                        modifier = Modifier
                            .padding(8.dp)  // Add padding to make the icon more visible
                            .size(30.dp)  // Adjust the size of the icon
                    )
                },
                alwaysShowLabel = false,  // Make sure the label is not shown
                selectedContentColor = MaterialTheme.colorScheme.secondary, // Color when selected
                unselectedContentColor = MaterialTheme.colorScheme.onPrimary // Color when unselected
            )
            BottomNavigationItem(
                selected = currentRoute(navController) == "recentReviews",
                onClick = { navController.navigate("recentReviews") },
                icon = {
                    Icon(
                        Icons.Filled.Star,
                        contentDescription = "Recent Reviews",
                        modifier = Modifier
                            .padding(8.dp)  // Add padding to make the icon more visible
                            .size(30.dp)  // Adjust the size of the icon
                    )
                },
                alwaysShowLabel = false,  // Make sure the label is not shown
                selectedContentColor = MaterialTheme.colorScheme.secondary, // Color when selected
                unselectedContentColor = MaterialTheme.colorScheme.onPrimary // Color when unselected
            )
            BottomNavigationItem(
                selected = currentRoute(navController) == "userReviews",
                onClick = { navController.navigate("userReviews") },
                icon = {
                    Icon(
                        Icons.Filled.Person,
                        contentDescription = "My Reviews",
                        modifier = Modifier
                            .padding(8.dp)  // Add padding to make the icon more visible
                            .size(30.dp)  // Adjust the size of the icon
                    )
                },
                alwaysShowLabel = false,  // Make sure the label is not shown
                selectedContentColor = MaterialTheme.colorScheme.secondary, // Color when selected
                unselectedContentColor = MaterialTheme.colorScheme.onPrimary // Color when unselected
            )
            BottomNavigationItem(
                selected = currentRoute(navController) == "settings",
                onClick = { navController.navigate("settings") },
                icon = {
                    Icon(
                        Icons.Filled.Settings,
                        contentDescription = "Settings",
                        modifier = Modifier
                            .padding(8.dp)  // Add padding to make the icon more visible
                            .size(30.dp)  // Adjust the size of the icon
                    )
                },
                alwaysShowLabel = false,  // Make sure the label is not shown
                selectedContentColor = MaterialTheme.colorScheme.secondary, // Color when selected
                unselectedContentColor = MaterialTheme.colorScheme.onPrimary // Color when unselected
            )
        }
    }

    // Helper function to get the current route for the navigation bar item selection
    private fun currentRoute(navController: NavHostController): String? {
        return navController.currentBackStackEntry?.destination?.route
    }}

