package com.example.travelmate

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.travelmate.ui.navigation.Routes
import com.example.travelmate.ui.navigation.TravelNavHost
import com.example.travelmate.ui.screens.auth.AuthViewModel

@Composable
fun TravelMateRoot() {
    val authVm: AuthViewModel = hiltViewModel()
    val start = if (authVm.isLoggedIn()) Routes.Feed.route else Routes.Login.route

    MaterialTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            TravelNavHost(startDestination = start)
        }
    }
}
