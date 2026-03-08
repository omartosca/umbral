# Umbral — Agent Guidelines

This file provides context and conventions for AI agents (Claude, GitHub Copilot, Gemini CLI, Cursor, etc.) working on the Umbral codebase.

For Claude-specific project instructions, see [CLAUDE.md](CLAUDE.md).

---

## Project Overview

**Umbral** is an Android app that blocks social media apps using NFC tags as a physical trigger. When a user taps their phone on a NFC tag placed at their door, the app blocks a configurable list of apps. Tapping again unlocks them.

**Status:** Development complete — Beta phase
**Architecture:** Clean Architecture + MVVM
**Language:** Kotlin 2.1
**Min SDK:** 26 (Android 8.0)

---

## Critical Naming Rules

### Kotlin Code — English only
- Classes: `PascalCase` (e.g., `BlockingProfile`, `NfcTagManager`)
- Functions & variables: `camelCase` (e.g., `startBlocking`, `profileId`)
- Packages: `lowercase` (e.g., `com.umbral.nfc`, `com.umbral.blocking`)

### Database — English only
- Table names: `snake_case` (e.g., `blocking_profiles`, `nfc_tags`)
- Column names: `snake_case` (e.g., `created_at`, `is_whitelisted`)
- **Never use Spanish in database identifiers**

### UI Strings — Spanish only
- All user-visible text must be in Spanish
- Always use `strings.xml` — never hardcode text in Compose
  - ✅ `stringResource(R.string.btn_save)` → "Guardar"
  - ❌ `Text("Save")` or `Text("Guardar")`

---

## Architecture

The project follows Clean Architecture with three main layers:

```
presentation/   ← ViewModels, Compose screens, components
    ↓ (consumes)
domain/         ← Models, repository interfaces, business logic
    ↓ (implements)
data/           ← Room entities, DAOs, repository implementations, services
```

**Key rules:**
- `presentation` never imports `data` directly — only `domain`
- `data` entities (e.g., `BlockingProfileEntity`) must not leak into `domain` or `presentation`
- `domain` uses its own models (e.g., `BlockingProfile`) separate from Room entities
- All dependency injection is via Hilt — no manual instantiation of repositories or managers

---

## Key Patterns

### ViewModel state
```kotlin
data class HomeUiState(
    val profiles: List<BlockingProfile> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)

class HomeViewModel @HiltViewModel constructor(
    private val profileRepository: ProfileRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()
}
```

### Repository pattern
- Domain layer defines the interface (e.g., `ProfileRepository`)
- Data layer implements it (e.g., `ProfileRepositoryImpl`)
- Hilt binds the interface to the implementation in a `@Module`

### Room DAO
- DAOs return `Flow<T>` for observable queries
- Use `suspend fun` for one-shot operations (insert, update, delete)
- Complex queries use `@Query` with explicit SQL — no raw SQLite

---

## Database (Room v6)

**File:** `app/src/main/java/com/umbral/data/local/database/UmbralDatabase.kt`

### Existing tables
| Table | Entity Class | Purpose |
|-------|-------------|---------|
| `blocking_profiles` | `BlockingProfileEntity` | User blocking profiles |
| `blocked_apps` | `BlockedAppEntity` | Apps blocked per profile |
| `nfc_tags` | `NfcTagEntity` | Registered NFC tags |
| `blocking_sessions` | `BlockingSessionEntity` | Active blocking sessions |
| `blocked_attempts` | `BlockedAttemptEntity` | Access attempt log |
| `blocking_events` | `BlockingEventEntity` | Unified event log |
| `companion` | `CompanionEntity` | Gamification companion state |
| `locations` | `LocationEntity` | Gamification map locations |
| `progress` | `ProgressEntity` | Player progression |
| `achievements` | `AchievementEntity` | Unlocked achievements |
| `decorations` | `DecorationEntity` | Unlocked decorations |
| `blocked_notifications` | `BlockedNotificationEntity` | Blocked notification log |

### Adding a migration
1. Increment the version constant in `UmbralDatabase.kt`
2. Add the migration script in `DatabaseMigrations.kt`
3. Add the migration to the `Room.databaseBuilder` call
4. Export schema: schemas are saved to `app/schemas/`

---

## Module Map

| Package | Purpose |
|---------|---------|
| `com.umbral.data` | DAOs, entities, repositories, services, NFC, QR |
| `com.umbral.domain` | Models, repository interfaces, business managers |
| `com.umbral.presentation` | ViewModels, 8 screens, 20+ Compose components |
| `com.umbral.expedition` | Gamification system (achievements, companions, map) |
| `com.umbral.glance` | 4 Jetpack Glance widgets |
| `com.umbral.notifications` | Blocked notification tracking |
| `com.umbral.di` | Hilt modules |

---

## Testing

- **Framework:** JUnit + MockK + Turbine + Robolectric
- **Unit tests:** `app/src/test/java/com/umbral/`
- **Instrumented tests:** `app/src/androidTest/java/com/umbral/`
- **Coverage:** JaCoCo configured (`app/jacoco.gradle`)

### Conventions
- Mock dependencies with `MockK` — avoid real implementations in unit tests
- Use `Turbine` to test `Flow` emissions
- Use `Robolectric` when Android framework is needed without a device
- Every DAO should have integration tests
- Every ViewModel should have state-transition tests

```kotlin
@Test
fun `activate profile emits active state`() = runTest {
    val viewModel = HomeViewModel(fakeRepository)
    viewModel.activateProfile(profileId = "abc")
    viewModel.uiState.test {
        val state = awaitItem()
        assertTrue(state.activeProfileId == "abc")
    }
}
```

---

## What to Avoid

- **Hardcoded strings** in Compose or XML — always use `strings.xml`
- **Raw SQLite** — use Room DAOs only
- **Business logic in ViewModels** — keep it in domain layer managers/use cases
- **Accessing `data` layer from `presentation`** — respect layer boundaries
- **Skipping migrations** — every schema change needs a migration script
- **Spanish identifiers** in code or database

---

## CI/CD

- **GitHub Actions:** `.github/workflows/android.yml` and `test.yml`
- Runs on push/PR to `main` and `develop`
- Pipeline: unit tests → lint → build debug APK
- **Firebase App Distribution** configured for release builds

## Commit Format

```
[Feat] Add NFC tag reading module
[Fix] Resolve crash on permission denial
[Docs] Update architecture decisions
[Test] Add ViewModel state transition tests
[Refactor] Extract blocking logic to domain layer
```

No `Co-Authored-By` or `Generated with` footers.
