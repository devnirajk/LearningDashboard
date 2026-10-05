# Learning Dashboard — Android

**Platform:** Android | **Language:** Kotlin | **UI:** Jetpack Compose

---

## 1. Architecture

**Choice: MVVM + Repository pattern (Clean Architecture lite)**

```
UI (Composables)
    ↓
ViewModel  (androidx.lifecycle.ViewModel)
    ↓
Repository interface  (domain layer)
    ↓
CourseRepositoryImpl  (data layer)
    ↙           ↘
RemoteDataSource    Room (AppDatabase)
(mock API)          (local cache)
```

**Why this choice:**
- ViewModels survive config changes; UI reacts to `StateFlow` — no manual lifecycle management.
- The `CourseRepository` interface sits in `domain/` so ViewModels depend on an abstraction, not a concrete DB or network class. Swapping Room for DataStore, or the mock for Retrofit, requires zero ViewModel changes.
- Room's `Flow`-returning DAOs give automatic UI refresh when the DB changes — progress updates propagate instantly without polling.
- No DI framework (Hilt) was added to keep scope small; the repository is constructed manually in ViewModels. In a production app, Hilt would replace this.

---

## 2. Offline Support

**Storage:** Room (SQLite) with two tables — `courses` and `lessons`.

**Flow:**
1. On first launch, `DashboardViewModel` calls `repository.refreshCourses()` which fetches from `RemoteDataSource` and writes to Room via `INSERT OR REPLACE`.
2. `observeCourses()` returns a `Flow<List<Course>>` backed by Room's `observeAll()` DAO query — the UI always reads from the local DB.
3. If the device is offline on subsequent launches, `refreshCourses()` throws (network error), but the Flow still emits the previously cached rows — the user sees their courses uninterrupted.
4. Lesson completion writes (`markLessonCompleted`) go directly to Room, so they persist offline immediately.

---

## 3. Security — Token Storage in Production

Auth tokens must **never** be stored in `SharedPreferences` (plaintext) or in the Room DB.

**Production approach:**
- Use `EncryptedSharedPreferences` from the Jetpack Security library (`androidx.security:security-crypto`). It wraps AES-256 GCM encryption backed by the Android Keystore.
- The Keystore key never leaves secure hardware on supported devices (API 23+).
- For highly sensitive apps, add biometric re-authentication before reading the token.
- Tokens should be short-lived JWTs; refresh tokens stored separately with a shorter TTL check.

---

## 4. Scale — 1 Million Users + Hundreds of Courses

Given that scale, five things I would improve:

1. **Pagination (Paging 3):** Load courses in pages (e.g. 20 at a time) instead of fetching all at once. `RemoteMediator` in Paging 3 handles the network→DB pipeline automatically.

2. **Background sync with WorkManager:** Replace the foreground `refreshCourses()` call with a `PeriodicWorkRequest`. Courses sync in the background even when the app is closed; UI always reads from the local cache.

3. **Dependency Injection (Hilt):** Replace manual construction of `AppDatabase` and `CourseRepositoryImpl` inside ViewModels with Hilt-injected singletons. This prevents multiple DB instances, improves testability, and removes boilerplate.

4. **Real network layer (Retrofit + OkHttp):** Replace `RemoteDataSource` with a Retrofit interface. Add an OkHttp interceptor to attach the auth token to every request, and a separate interceptor for retry + exponential backoff on transient failures.

5. **Conflict-free offline writes:** For lesson completion, queue writes in a `SyncQueue` Room table when offline, then replay them against the server when connectivity returns (using a `ConnectivityManager` callback + WorkManager constraint). This prevents silent data loss on flaky connections.

---

## 5. Second Platform — iOS/macOS (Swift + SwiftUI)

The same architecture maps cleanly:

| Android | iOS equivalent |
|---|---|
| Jetpack Compose | SwiftUI |
| ViewModel + StateFlow | `@Observable` class (iOS 17+) or `ObservableObject` + `@Published` |
| Room | SwiftData (iOS 17+) or Core Data |
| Repository interface | Swift `protocol` |
| Coroutines + Flow | Swift Concurrency (`async/await`, `AsyncStream`) |
| Navigation Compose | `NavigationStack` + `.navigationDestination` |
| EncryptedSharedPreferences | Keychain via `SecItemAdd` / `Security` framework |

**Implementation steps:**
1. Define the same `CourseRepository` protocol in Swift.
2. Implement `CourseRepositoryImpl` using SwiftData for the cache and `URLSession` for the mock API.
3. Each screen has an `@Observable` ViewModel that calls the repository and drives SwiftUI state.
4. Navigation uses `NavigationStack` with a `NavigationPath` — equivalent to Compose's `NavHost`.
5. Unit tests use `XCTest`; mock the repository protocol with a `MockCourseRepository` struct.

---

## Running the App

```
# Debug build
./gradlew assembleDebug

# Unit tests
./gradlew testDebugUnitTest
```

**Demo credentials:** `user@example.com` / `password123`
