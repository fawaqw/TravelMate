# Architecture Overview

This project follows the MVVM (Model-View-ViewModel) architectural pattern combined with Clean Architecture principles to ensure separation of concerns, maintainability, and testability.

## Layers

### 1. Presentation Layer (UI)
- Developed using Jetpack Compose.
- Screens: CityList, CityDetail, Favorites, Reviews, Search.
- ViewModels: Handle UI state and interact with the Domain layer using Coroutines.

### 2. Domain Layer (Business Logic)
- Contains Entity models (Place, Favorite, Review).
- Repository Interfaces: Define the contract for data operations.
- Use Cases (optional): Specific business rules.

### 3. Data Layer (Implementation)
- Repositories: Implementation of domain interfaces.
- Remote Data Source: Retrofit for GeoDB API and Firebase Realtime Database for reviews and favorites.
- Local Data Source: Room database for caching city information.
- Mappers: Functions to convert between DTOs, Entities, and Domain models.

## Data Flow
1. UI triggers an action in the ViewModel.
2. ViewModel calls a function in the Repository.
3. Repository decides whether to fetch data from Local (Room) or Remote (Retrofit/Firebase).
4. Data is mapped to Domain models and returned to the ViewModel.
5. ViewModel updates the UI state (StateFlow), which triggers a recomposition of the UI.

## Technologies Used
- Dependency Injection: Hilt
- Network: Retrofit + OkHttp
- Local Database: Room
- Async: Kotlin Coroutines + Flow
- Navigation: Compose Navigation
- Backend: Firebase (Auth, Analytics, Realtime Database)
