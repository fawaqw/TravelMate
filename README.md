# TravelMate - Android Travel Discovery App

TravelMate is a modern Android application designed for travelers to discover new places, read reviews, and share their experiences. The project is built using modern Android development practices, including Jetpack Compose, MVVM architecture, and Firebase integration.

## Features

### 1. Screens and Navigation
- **Feed/List Screen**: Browse travel destinations with high-quality images and real-time average ratings.
- **Details Screen**: In-depth info, description, and user reviews with author names.
- **Search Screen**: Hybrid search (Local + Firebase + Remote API) with debouncing.
- **Authentication**: Secure Sign-In and Sign-Up (including Name/Nickname) via Firebase Auth.
- **Profile Screen**: User info, session management, and "My Reviews" section with deletion logic.
- **Review System**: Add reviews with star ratings. Integrated Real-time DB updates.

### 2. Architecture & Tech Stack
- **UI**: Jetpack Compose
- **Architecture**: MVVM + Repository pattern.
- **Dependency Injection**: Hilt (Dagger).
- **Concurrency**: Kotlin Coroutines & Flow.

### 3. Data & Networking
- **Backend**: Firebase Realtime Database & Auth.
- **Offline-First**: Room DB local caching for cities.
- **Image Loading**: Coil (Async loading).

## Testing & Stability

### Unit Tests
Located in `app/src/test/java/com/example/travelmate/BusinessLogicTest.kt`.
1. `cityEntityToDomain calculates rating`: Validates the business logic for initial rating generation.
2. `cityEntityToDomain formats name`: Ensures correct data transformation for UI.
3. `review deletion policy (Allowed)`: Tests that reviews < 24h old can be deleted.
4. `review deletion policy (Forbidden)`: Tests that reviews > 24h old cannot be deleted.
5. `average rating calculation`: Validates the logic for aggregating user scores.

### Manual Test Checklist
1. [ ] **Auth**: Sign up with name, email, and password. Check if name appears in profile.
2. [ ] **Auth**: Login with wrong credentials (should show error message).
3. [ ] **Feed**: Scroll down to trigger pagination (if > 10 items).
4. [ ] **Search**: Type "Paris" and wait 500ms (debounce). Verify results appear.
5. [ ] **Details**: Open a place, verify image and description load.
6. [ ] **Reviews**: Add a 5-star review. Verify it appears instantly in the list.
7. [ ] **Real-time**: Add a review and check if the average rating on the Feed card updates.
8. [ ] **Profile**: Verify "My Reviews" displays correct place names and delete icons.
9. [ ] **Deletion**: Delete a review created less than 24h ago. Verify it disappears.
10. [ ] **Offline**: Disable internet. Verify cached places are still visible in Feed.

## Configuration
1. Add `google-services.json` to `app/`.
2. Enable Email/Password Auth & Realtime DB in Firebase.
3. Replace API Key in `NetworkModule.kt` for GeoDB.
