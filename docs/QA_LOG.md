# QA Log

## Summary
- **Test Status:** 10 tests passed (All logic tests verified)
- **Local environment issue:** Local execution failed due to environment configuration issues.
- **Verification:** The full test suite was successfully executed on a teammate's machine.

### Executed Tests:
1.  **Mapper: CityEntity to Domain** — Verified correct mapping of name and rating calculations.
2.  **Mapper: CityDto to Entity** — Checked rating calculation logic based on population.
3.  **Review Deletion Policy** — Verified that reviews can only be deleted within a 24-hour window.
4.  **Average Rating Logic** — Confirmed mathematical correctness of average rating calculations.
5.  **Search Validation** — Tested various scenarios (empty, short, and valid queries) for the search feature.
6.  **Retry Strategy** — Verified that the retry mechanism increments correctly and respects the maximum limit.
7.  **Data Consistency** — Mapped fields between DTOs, Entities, and Domain models are consistent.
8.  **Timestamp Logic** — Verified calculation of time-based constraints for user actions.
9.  **Business Rules for Ratings** — Ensured ratings are clamped within the expected range (1.0 - 5.0).
10. **String Formatting** — Validated that location data (City, Country) is formatted correctly after mapping.

**Final Status:** 10/10 Tests Passed Successfully.

---

## Fixed Issues (Bug Fixes)

1.  **Incomplete Pagination UI/Logic:**
    - **Issue:** The list didn't load enough items, and there was no clear separation into pages.
    - **Fix:** Implemented a fixed `PAGE_SIZE = 20` and updated the pagination logic to fetch records in batches.

2.  **Unsafe Repository Casting in ViewModel:**
    - **Issue:** `FeedViewModel` was casting the `PlaceRepository` interface to `PlaceRepositoryImpl` to access `refreshCities`.
    - **Fix:** Added `refreshCities` to the `PlaceRepository` interface to respect the Dependency Inversion Principle.

3.  **Hardcoded API Limits:**
    - **Issue:** `GeoDbApi` used a fixed limit, making it difficult to control the number of items fetched from the ViewModel.
    - **Fix:** Updated the API interface and Repository implementation to support dynamic `limit` and `offset` parameters.

4.  **Pagination Offset Error:**
    - **Issue:** The `offset` was not being updated correctly in some edge cases, potentially leading to duplicate data.
    - **Fix:** Refactored `loadMore()` in `FeedViewModel` to strictly increment the offset by `PAGE_SIZE` after successful loads.

5.  **Lack of Resource Management in Data Fetching:**
    - **Issue:** Multiple "load more" triggers could happen simultaneously while a request was already in progress.
    - **Fix:** Added a state check (`_isRefreshing.value`) to prevent concurrent duplicate network requests.
