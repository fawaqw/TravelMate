package com.example.travelmate

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.travelmate.ui.navigation.Routes
import com.exaple.travelmate.ui.screens.feed.FeedViewModel


@Composable
fun FeedScreen(navController: NavController, vm: FeedViewModel = hiltViewModel()) {

    val places by vm.places.collectAsState()

    LazyColumn {
        items(places) { place ->
            Text(
                text = place.name,
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        navController.navigate(Routes.Details.create(place.id))
                    }
                    .padding(16.dp)
            )
        }
    }
}
