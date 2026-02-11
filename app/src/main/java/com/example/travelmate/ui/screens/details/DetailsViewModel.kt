package com.example.travelmate.ui.screens.details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.travelmate.domain.model.Place
import com.example.travelmate.domain.repository.PlaceRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailsViewModel @Inject constructor(
    private val repo: PlaceRepository
) : ViewModel() {

    private val _place = MutableStateFlow<Place?>(null)
    val place: StateFlow<Place?> = _place

    fun load(id: String) {
        viewModelScope.launch {
            _place.value = repo.getPlaceById(id)
        }
    }
}