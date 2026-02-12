# TravelMate - Android Travel Discovery App 

TravelMate is a feature-rich Android application designed for travelers to discover global destinations, manage a personal list of favorites, and participate in a real-time review community.

## Key Features

### 1. Screens and Navigation (Criteria 4.1)
- **Feed Screen**: Explore places with Lottie animations, real-time ratings, and an offline-mode indicator.
- **Details Screen**: Comprehensive place info, real-time heart toggle for favorites, and a list of user reviews with author names.
- **Search Screen**: Hybrid search (Firebase + Room + GeoDB) with debounced input (500ms) to optimize performance.
- **Profile Screen**: Displays user info, a horizontal "Favorite Places" list, and a history of personal reviews.
- **Auth (Login/SignUp)**: Secure flows with Firebase Auth. SignUp now captures user's Full Name.
- **Forms & Validation**: Login and SignUp screens feature real-time field validation with user-friendly error messages under the fields.

### 2. Real-time & User Data (Criteria 4.3 & 4.6)
- **Favorites System**: Each user has a unique "Favorites" list stored in Firebase. Status syncs instantly between the Feed and Details screens.
- **Reviews Community**: CRUD operations for reviews. Users can see names of authors.
- **Business Rule (Criteria 4.2)**: Users can only delete their own reviews within **24 hours** of posting.
- **Dynamic Scoring**: The rating displayed on each place card is a real-time average calculated from all user reviews in Firebase.

### 3. Advanced Networking & Persistence (Criteria 4.4 & 4.5)
- **Offline-First**: All places fetched from Firebase/API are mirrored to a local **Room Database**.
- **Seamless Sync**: App detects connection loss using a `ConnectivityObserver` and displays an "Offline Mode" banner, serving cached data.
- **UI States**: Professional states for Loading (Lottie), Success, Empty (Lottie), and Error with retry logic.

### 4. Technical Stack
- **Architecture**: Clean MVVM + Repository pattern.
- **Dependency Injection**: Hilt (Dagger).
- **Concurrency**: Kotlin Coroutines & Flow/StateFlow for reactive data.
- **Libraries**: 
  - **Coil**: Async image loading.
  - **Lottie**: High-quality UI animations.
  - **Retrofit**: GeoDB API integration.
  - **Firebase**: Auth & Realtime Database.

## Testing & Stability (Criteria 4.9)
- **Unit Tests**: 5 comprehensive tests in `BusinessLogicTest.kt` covering Mappers, Deletion Policy (24h rule), and Rating Calculations.
  - **Robustness**: Handled common scenarios like wrong credentials, no internet, and empty API responses without crashes.

## Configuration Steps

1. **Firebase Setup**:
   - Place `google-services.json` in the `app/` folder.
   - Enable **Email/Password Authentication** in Firebase Console.
   - Create a **Realtime Database** and set rules to allow read/write for authenticated users.
2. **API Key**:
   - In `com.example.travelmate.di.NetworkModule`, replace `PUT_YOUR_KEY_HERE` with your RapidAPI key for the GeoDB API.
3. **Build**:
   - Sync Gradle and run the `:app` module.

## Manual Test Checklist
1. [x] **Sign Up**: Register a new user with a name. Verify name appears in Profile.
2. [x] **Login Validation**: Try logging in with a short password (<6 chars). Check error message.
3. [x] **Real-time Favorites**: Heart a place in Feed, open Details — verify it's also hearted there.
4. [x] **Average Rating**: Add a 1-star review to a place. Verify its Feed rating drops instantly.
5. [x] **Search Debounce**: Type slowly in Search. Verify results update after you stop typing.
6. [x] **Offline Banner**: Turn off Wi-Fi. Verify "Offline Mode" banner appears and data remains.
7. [x] **Review Deletion**: Post a review and delete it. Verify it works.
8. [x] **24h Policy**: Manually set an old timestamp in Firebase for a review. Verify the delete icon disappears in the app.
9. [x] **Navigation**: Log out from Profile. Verify you are redirected to Login and can't go "back" to Profile.
10. [x] **Lottie**: Open the app and observe the "Searching..." animation before data loads.
