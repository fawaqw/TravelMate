package com.example.travelmate.ui.screens.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.travelmate.domain.repository.PlaceRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject


@HiltViewModel
class SearchViewModel @Inject constructor(
    private val repo: PlaceRepository
) : ViewModel() {

    private val query = MutableStateFlow("")

    val uiState = query
        .debounce(500)
        .filter { it.isNotBlank() }
        .flatMapLatest { q ->
            flow {
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
        .stateIn(viewModelScope, SharingStarted.Lazily, UiState.Empty)

    fun onQueryChange(new: String) {
        query.value = new
    }
}
