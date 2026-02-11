package com.example.travelmate.ui.screens.feed

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.travelmate.ui.navigation.Routes
import androidx.compose.runtime.getValue




@Composable
fun FeedScreen(
    navController: NavController,
    vm: FeedViewModel = hiltViewModel()
) {
    val places by vm.places.collectAsStateWithLifecycle()

    LazyColumn {
        itemsIndexed(places) { index, place ->

            if (index == places.lastIndex) {
                vm.loadMore()
            }

            Text(
                text = place.name,
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        navController.navigate(
                            Routes.Details.create(place.id)
                        )
                    }
                    .padding(16.dp)
            )
        }
    }
}
