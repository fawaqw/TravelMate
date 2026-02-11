package com.example.travelmate.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.*
import com.example.travelmate.ui.screens.feed.FeedScreen
import com.example.travelmate.ui.screens.*
import com.example.travelmate.ui.screens.auth.LoginScreen
import com.example.travelmate.ui.screens.details.DetailsScreen
import com.example.travelmate.ui.screens.review.CreateReviewScreen
import com.example.travelmate.ui.screens.profile.ProfileScreen

@Composable
fun TravelNavHost() {
    val navController = rememberNavController()

    NavHost(navController, startDestination = Routes.Login.route)    {

        composable(Routes.Login.route) {
            LoginScreen(navController)
        }


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

        composable(Routes.CreateReview.route) { backStack ->
            val id = backStack.arguments?.getString("placeId")!!
            CreateReviewScreen(navController, id)
        }

    }
}
