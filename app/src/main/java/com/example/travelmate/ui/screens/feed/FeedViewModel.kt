package com.example.travelmate.ui.screens.feed

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.travelmate.data.repository.PlaceRepositoryImpl
import com.example.travelmate.domain.repository.PlaceRepository
import com.example.travelmate.ui.components.UiState
import com.example.travelmate.util.ConnectivityObserver
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject
import com.example.travelmate.domain.model.Place
import com.google.firebase.database.FirebaseDatabase

@HiltViewModel
class FeedViewModel @Inject constructor(
    private val repo: PlaceRepository,
    private val connectivityObserver: ConnectivityObserver
) : ViewModel() {

    private var offset = 0
    private val _isRefreshing = MutableStateFlow(false)
    val isRefreshing = _isRefreshing.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error = _error.asStateFlow()

    val networkStatus = connectivityObserver.observe()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), ConnectivityObserver.Status.Available)

    val places: StateFlow<List<Place>> = repo.getPlaces()
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    val uiState: StateFlow<UiState<List<Place>>> = combine(places, _isRefreshing, _error) { list, loading, err ->
        when {
            loading && list.isEmpty() -> UiState.Loading
            err != null && list.isEmpty() -> UiState.Error
            list.isEmpty() && !loading -> UiState.Empty
            else -> UiState.Success(list)
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), UiState.Loading)

    init {
        addTestPlaces()
        loadMore()
    }

    fun toggleFavorite(placeId: String) {
        viewModelScope.launch {
            repo.toggleFavorite(placeId)
        }
    }

    fun loadMore() {
        if (_isRefreshing.value) return
        
        viewModelScope.launch {
            _isRefreshing.value = true
            _error.value = null
            try {
                (repo as PlaceRepositoryImpl).refreshCities(offset)
                offset += 20
            } catch (e: Exception) {
                _error.value = e.localizedMessage ?: "Unknown error"
            } finally {
                _isRefreshing.value = false
            }
        }
    }

    fun addTestPlaces() {
        val database = FirebaseDatabase.getInstance().getReference("places")
        val testPlaces = listOf(
            Place(
                id = "1",
                name = "Eiffel Tower",
                city = "Paris",
                country = "France",
                imageUrl = "https://images.unsplash.com/photo-1511739001486-6bfe10ce785f",
                description = "The Eiffel Tower is a wrought-iron lattice tower on the Champ de Mars in Paris, France. It was constructed from 1887 to 1889 as the centerpiece of the 1889 World's Fair. Although initially criticized by some of France's leading artists and intellectuals for its design, it has since become a global cultural icon of France and one of the most recognizable structures in the world.",
                rating = 4.8,
                category = "Landmark"
            ),
            Place(
                id = "2",
                name = "Colosseum",
                city = "Rome",
                country = "Italy",
                imageUrl = "https://images.unsplash.com/photo-1552832230-c0197dd311b5",
                description = "The Colosseum is an oval amphitheatre in the centre of the city of Rome, Italy, just east of the Roman Forum. It is the largest ancient amphitheatre ever built, and is still the largest standing amphitheatre in the world today, despite its age.",
                rating = 4.7,
                category = "Historical"
            ),
            Place(
                id = "3",
                name = "Machu Picchu",
                city = "Cusco Region",
                country = "Peru",
                imageUrl = "https://images.unsplash.com/photo-1526392060635-9d6019884377",
                description = "Machu Picchu is a 15th-century Inca citadel located in the Eastern Cordillera of southern Peru on a 2,430-meter mountain ridge. It is often referred to as the 'Lost City of the Incas'.",
                rating = 4.9,
                category = "Adventure"
            ),
            Place(
                id = "4",
                name = "Great Wall of China",
                city = "Beijing",
                country = "China",
                imageUrl = "https://images.unsplash.com/photo-1508804185872-d7badad00f7d",
                description = "The Great Wall of China is a series of fortifications that were built across the historical northern borders of ancient Chinese states and Imperial China as protection against various nomadic groups.",
                rating = 4.6,
                category = "Historical"
            ),
            Place(
                id = "5",
                name = "Santorini",
                city = "Oia",
                country = "Greece",
                imageUrl = "https://images.unsplash.com/photo-1570077188670-e3a8d69ac5ff",
                description = "Santorini is an island in the southern Aegean Sea, about 200 km southeast of the Greek mainland. It is the largest island of a small, circular archipelago, which bears the same name and is the remnant of a volcanic caldera.",
                rating = 4.9,
                category = "Vacation"
            )
        )

        testPlaces.forEach { place ->
            database.child(place.id).setValue(place)
        }
    }
}
