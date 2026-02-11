package com.example.travelmate.ui.screens.review

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateReviewScreen(
    navController: NavController,
    placeId: String,
    vm: CreateReviewViewModel = hiltViewModel()
) {
    var text by remember { mutableStateOf("") }
    var rating by remember { mutableStateOf(0) }
    val userId = FirebaseAuth.getInstance().currentUser?.uid ?: ""
    val placeName by vm.placeName.collectAsState()

    LaunchedEffect(placeId) {
        vm.loadPlaceName(placeId)
    }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Write a Review") })
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                "How was your visit to",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.secondary
            )
            Text(
                placeName.ifBlank { "Loading..." },
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )
            
            Spacer(Modifier.height(24.dp))

            Text(
                "Tap to rate:",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                (1..5).forEach { index ->
                    Icon(
                        imageVector = if (index <= rating) Icons.Filled.Star else Icons.Outlined.Star,
                        contentDescription = null,
                        tint = if (index <= rating) Color(0xFFFFB400) else Color.Gray,
                        modifier = Modifier
                            .size(56.dp)
                            .clickable { rating = index }
                            .padding(4.dp)
                    )
                }
            }

            Spacer(Modifier.height(24.dp))

            OutlinedTextField(
                value = text,
                onValueChange = { text = it },
                label = { Text("Your thoughts") },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp),
                placeholder = { Text("Tell us about your experience...") },
                shape = MaterialTheme.shapes.medium
            )

            Spacer(Modifier.height(32.dp))

            Button(
                onClick = {
                    vm.submit(placeId, text, rating, userId)
                    navController.popBackStack()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                enabled = text.isNotBlank() && rating > 0,
                shape = MaterialTheme.shapes.medium
            ) {
                Text("Submit Review", style = MaterialTheme.typography.titleMedium)
            }
        }
    }
}
