# Release Notes

## Version 1.0.0 (Final)

### Changes from Endterm to Final
- Optimized performance by adding database indexing.
- Increased unit test coverage to 10 tests covering business logic and data mapping.
- Implemented robust pagination (20 items per page).
- Fixed critical repository implementation issues (Dependency Inversion).
- Configured separate Release and Debug build variants.

## Release Checklist
1.  **Install:** App installs successfully on target API levels (24+).
2.  **Launch:** App reaches Home screen in under 2 seconds.
3.  **Login:** User can authenticate via Firebase (Email/Password).
4.  **Login Failure:** Clear error message shown for wrong password/email.
5.  **Session Persistence:** App remembers user session after being killed.
6.  **Offline Mode:** Cached cities are accessible without internet.
7.  **Offline Favorites:** Previously starred places remain in Favorites list while offline.
8.  **Connectivity Handling:** App detects network loss and shows a "No Connection" indicator.
9.  **Realtime Sync:** Favorites toggle reflects across screens instantly.
10. **Realtime Reviews:** New reviews appear without manual refresh (Firebase listener).
11. **Pagination:** Feed loads exactly 20 items per request.
12. **Infinite Scroll:** Reaching the bottom triggers a successful load of the next page.
13. **Error States:** "Retry" button appears if GeoDB API call fails.
14. **Search Validation:** Empty or short (<2 chars) queries are handled gracefully.
15. **Memory Management:** No OOM errors during heavy image scrolling (Coil verified).

# Performance Note

## Improvements
1. **Database Indexing:** Added indices to `name` and `country` columns in the Room database to speed up search queries and filtering.
2. **Efficient Image Loading:** Utilized Coil for asynchronous image loading, automatic memory/disk caching, and downsampling to prevent OOM errors.
3. **Asynchronous Operations:** All database and network operations are performed using Kotlin Coroutines on `Dispatchers.IO` to keep the UI thread responsive.

## Evidence
The UI remains responsive at 60 FPS during list scrolling and searching. Memory usage is stable due to efficient caching.
(See /docs/performance_evidence.png for Profiler data).

### Features
- Explore popular travel destinations.
- Search for cities worldwide.
- Read and write user reviews.
- Add cities to a personal favorites list.
- Secure user account management.

### Known Limitations
- Real-time updates for reviews may have a slight delay depending on network conditions.
- Offline mode is limited to previously cached city data.
- Search results are limited to the top cities provided by the GeoDB API.
