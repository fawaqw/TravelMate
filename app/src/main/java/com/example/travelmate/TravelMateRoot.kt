package com.example.travelmate

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.example.travelmate.ui.navigation.Routes
import com.example.travelmate.ui.navigation.TravelNavHost
import com.example.travelmate.ui.screens.auth.AuthViewModel

@Composable
fun TravelMateRoot() {
    val authVm: AuthViewModel = hiltViewModel()
    val start = if (authVm.isLoggedIn()) Routes.Feed.route else Routes.Login.route

    MaterialTheme {
        val navController = rememberNavController()
        NavHost(navController, startDestination = start) {
        }
    }
}


