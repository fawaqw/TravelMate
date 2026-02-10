package com.example.travelmate

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.example.travelmate.ui.navigation.TravelNavHost

@Composable
fun TravelMateRoot() {
    MaterialTheme {
        TravelNavHost()
    }
}

