package com.example.travelmate.ui.screens.feed

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.travelmate.data.repository.PlaceRepositoryImpl
import com.example.travelmate.domain.repository.PlaceRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject
import com.example.travelmate.domain.model.Place
import kotlinx.coroutines.flow.StateFlow


@HiltViewModel
class FeedViewModel @Inject constructor(
    private val repo: PlaceRepository
) : ViewModel() {

    private var offset = 0

    val places: StateFlow<List<Place>> =
        repo.getPlaces().stateIn(
            viewModelScope,
            SharingStarted.Lazily,
            emptyList()
        )

    init {
        loadMore()
    }

    fun loadMore() {
        viewModelScope.launch {
            (repo as PlaceRepositoryImpl).refreshCities(offset)
            offset += 20
        }
    }
}
