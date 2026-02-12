package com.example.travelmate.ui.screens.details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.travelmate.domain.model.Place
import com.example.travelmate.domain.repository.PlaceRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailsViewModel @Inject constructor(
    private val repo: PlaceRepository
) : ViewModel() {

    private val _placeId = MutableStateFlow<String?>(null)
    
    val place: StateFlow<Place?> = _placeId
        .flatMapLatest { id ->
            repo.getPlaces().map { places ->
                places.firstOrNull { it.id == id }
            }
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    fun load(id: String) {
        _placeId.value = id
    }

    fun toggleFavorite() {
        val id = _placeId.value ?: return
        viewModelScope.launch {
            repo.toggleFavorite(id)
        }
    }
}
