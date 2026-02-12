package com.example.travelmate.ui.screens.details

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.travelmate.ui.navigation.Routes
import com.exaple.travelmate.ui.screens.details.ReviewsViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailsScreen(
    navController: NavController,
    id: String,
    vm: DetailsViewModel = hiltViewModel(),
    reviewsVm: ReviewsViewModel = hiltViewModel()
) {
    val place by vm.place.collectAsState()
    val reviews by reviewsVm.reviews(id).collectAsState()

    LaunchedEffect(id) {
        vm.load(id)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(place?.name ?: "Details") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    place?.let { p ->
                        IconButton(onClick = { vm.toggleFavorite() }) {
                            Icon(
                                imageVector = if (p.isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                                contentDescription = "Favorite",
                                tint = if (p.isFavorite) Color.Red else LocalContentColor.current
                            )
                        }
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    navController.navigate(Routes.CreateReview.route.replace("{placeId}", id))
                }
            ) {
                Text("+", modifier = Modifier.padding(horizontal = 16.dp), style = MaterialTheme.typography.headlineSmall)
            }
        }
    ) { padding ->
        place?.let { p ->
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
            ) {
                item {
                    AsyncImage(
                        model = p.imageUrl,
                        contentDescription = null,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(250.dp),
                        contentScale = ContentScale.Crop
                    )
                }

                item {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = p.name,
                            style = MaterialTheme.typography.headlineMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "${p.city}, ${p.country}",
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.secondary
                        )
                        Spacer(Modifier.height(8.dp))
                        SuggestionChip(
                            onClick = {},
                            label = { Text(p.category) }
                        )
                        Spacer(Modifier.height(16.dp))
                        Text(
                            text = p.description,
                            style = MaterialTheme.typography.bodyLarge
                        )
                        Spacer(Modifier.height(24.dp))
                        Divider()
                        Spacer(Modifier.height(16.dp))
                        Text(
                            text = "Reviews",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                if (reviews.isEmpty()) {
                    item {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(32.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("No reviews yet. Be the first to add one!", style = MaterialTheme.typography.bodyMedium)
                        }
                    }
                } else {
                    items(reviews) { review ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp, vertical = 8.dp)
                        ) {
                            Column(modifier = Modifier.padding(12.dp)) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(Icons.Default.Person, contentDescription = null, modifier = Modifier.size(20.dp), tint = MaterialTheme.colorScheme.primary)
                                    Spacer(Modifier.width(8.dp))
                                    Text(
                                        text = review.userName.ifBlank { "Traveler" },
                                        fontWeight = FontWeight.Bold,
                                        style = MaterialTheme.typography.titleMedium
                                    )
                                }
                                Spacer(Modifier.height(4.dp))
                                Text(
                                    text = "⭐ ".repeat(review.rating),
                                    color = Color(0xFFFFB400)
                                )
                                Spacer(Modifier.height(4.dp))
                                Text(text = review.text, style = MaterialTheme.typography.bodyMedium)
                            }
                        }
                    }
                }
                
                item {
                    Spacer(Modifier.height(80.dp)) // Padding for FAB
                }
            }
        } ?: Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
    }
}
