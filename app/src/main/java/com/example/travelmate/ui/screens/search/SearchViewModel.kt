package com.example.travelmate.ui.screens.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.travelmate.domain.repository.PlaceRepository
import com.example.travelmate.ui.components.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject
import com.example.travelmate.domain.model.Place
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow


@HiltViewModel
class SearchViewModel @Inject constructor(
    private val repo: PlaceRepository
) : ViewModel() {

    private val _query = MutableStateFlow("")
    val query = _query.asStateFlow()

    val uiState: StateFlow<UiState<List<Place>>> = _query
        .debounce(500)
        .flatMapLatest { q ->
            flow {
                if (q.isBlank()) {
                    emit(UiState.Success(emptyList()))
                    return@flow
                }
                emit(UiState.Loading)
                try {
                    val result = repo.searchPlaces(q)
                    if (result.isEmpty()) emit(UiState.Empty)
                    else emit(UiState.Success(result))
                } catch (e: Exception) {
                    emit(UiState.Error)
                }
            }
        }
        .stateIn(viewModelScope, SharingStarted.Lazily, UiState.Success(emptyList()))

    fun onQueryChange(new: String) {
        _query.value = new
    }
}
