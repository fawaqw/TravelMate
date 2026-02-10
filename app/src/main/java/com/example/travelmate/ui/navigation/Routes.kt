package com.example.travelmate.ui.navigation

sealed class Routes(val route: String) {
    object Feed : Routes("feed")
    object Details : Routes("details/{placeId}") {
        fun create(placeId: String) = "details/$placeId"
    }
    object Search : Routes("search")
    object CreateReview : Routes("create_review/{placeId}")
    object Profile : Routes("profile")
}
