# TravelMate - Android Travel Discovery App (Final Version)

TravelMate is a feature-rich Android application designed for travelers to discover global destinations, manage a personal list of favorites, and participate in a real-time review community.

## Architecture Overview
The project follows **Clean Architecture** with **MVVM** pattern.
- **Presentation:** Jetpack Compose + ViewModels (StateFlow).
- **Domain:** UseCases and Repository interfaces.
- **Data:** Room (Local), Retrofit & Firebase (Remote).
- **Dependency Injection:** Hilt.

Detailed documentation: [ARCHITECTURE.md](ARCHITECTURE.md)

## Key Features
- **Feed & Details:** Explore cities with real-time ratings and Lottie animations.
- **Search:** Hybrid search (Local + Remote) with input validation.
- **Favorites:** Real-time sync with Firebase Realtime Database.
- **Community:** Post and read reviews (24h deletion policy logic).
- **Offline Support:** Local caching with Room and connectivity status indicator.

## Performance Improvements
1. **DB Indexing:** Optimized Room queries for city names and countries.
2. **Efficient Loading:** Image downsampling and memory caching via Coil.
3. **Responsiveness:** Asynchronous data flow using Kotlin Coroutines (Dispatchers.IO).


## Testing & Quality
- **Unit Tests:** 10 tests total in `BusinessLogicTest.kt`.
  - 5 Endterm tests (Mappers, 24h Deletion, Ratings).
  - 5 Final tests (Population Mapping, Search Validation, Retry Strategy).
- **QA Log:** [QA_LOG.md](QA_LOG.md)
- **Release Checklist:** [RELEASE_CHECKLIST.md](RELEASE_CHECKLIST.md)

## Build & Run Steps
1. **Firebase:** Add `google-services.json` to the `app/` directory.
2. **API Key:** Add your GeoDB API key in `NetworkModule.kt` (X-RapidAPI-Key).
3. **Credentials:** Use any email/password for Firebase Auth.
4. **Build:** Run `./gradlew assembleDebug` or use Android Studio "Run" button.


## Release Notes
What's new in the Final version: [RELEASE_NOTES.md](RELEASE_NOTES.md)
