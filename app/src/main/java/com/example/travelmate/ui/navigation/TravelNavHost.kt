package com.example.travelmate.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.*
import com.example.travelmate.ui.screens.*

@Composable
fun TravelNavHost() {
    val navController = rememberNavController()

    NavHost(navController, startDestination = Routes.Feed.route) {

        composable(Routes.Feed.route) {
            FeedScreen(navController)
        }

        composable(Routes.Details.route) { backStack ->
            val id = backStack.arguments?.getString("placeId")!!
            DetailsScreen(navController, id)
        }

        composable(Routes.Search.route) {
            SearchScreen(navController)
        }

        composable(Routes.CreateReview.route) { backStack ->
            val id = backStack.arguments?.getString("placeId")!!
            CreateReviewScreen(navController, id)
        }

        composable(Routes.Profile.route) {
            ProfileScreen()
        }
    }
}
