# Architecture Rules

## Layer Boundaries

The project follows Clean Architecture with three layers. Dependencies flow **inward only**:

```
presentation → domain ← data
```

- `presentation` imports `domain` — never imports `data` directly
- `data` implements `domain` interfaces
- `domain` has zero Android framework dependencies (pure Kotlin)

### What goes where

| Layer | Contains | Must NOT contain |
|-------|----------|-----------------|
| `presentation` | ViewModels, Compose screens, components, Navigation | Room entities, DAOs, direct DB access |
| `domain` | Models (`BlockingProfile`), repository interfaces, business managers | Room annotations, Android Context, implementation details |
| `data` | Room entities, DAOs, repository implementations, services, NFC/QR managers | UI logic, ViewModel, Compose |

## Models vs Entities

Domain models and Room entities are **separate classes**.

```kotlin
// domain/blocking/BlockingProfile.kt — domain model
data class BlockingProfile(
    val id: String,
    val name: String,
    val blockedApps: List<String>
)

// data/local/entity/BlockingProfileEntity.kt — Room entity
@Entity(tableName = "blocking_profiles")
data class BlockingProfileEntity(
    @PrimaryKey val id: String,
    val name: String,
    val blockedAppsJson: String  // serialized
)
```

Map between them in the repository implementation — never expose entities outside `data`.

## ViewModels

- Annotate with `@HiltViewModel` and inject via constructor
- Expose state as `StateFlow<UiState>` — never `LiveData` or raw mutable state
- Collect from repositories with `viewModelScope`
- One `UiState` data class per ViewModel

```kotlin
@HiltViewModel
class ProfilesViewModel @Inject constructor(
    private val profileRepository: ProfileRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(ProfilesUiState())
    val uiState: StateFlow<ProfilesUiState> = _uiState.asStateFlow()
}
```

## Repository Pattern

Domain layer defines the interface:

```kotlin
// domain/blocking/ProfileRepository.kt
interface ProfileRepository {
    fun getProfiles(): Flow<List<BlockingProfile>>
    suspend fun saveProfile(profile: BlockingProfile)
    suspend fun deleteProfile(id: String)
}
```

Data layer implements it:

```kotlin
// data/blocking/ProfileRepositoryImpl.kt
class ProfileRepositoryImpl @Inject constructor(
    private val dao: BlockingProfileDao
) : ProfileRepository { ... }
```

Hilt binds them:

```kotlin
@Binds
abstract fun bindProfileRepository(impl: ProfileRepositoryImpl): ProfileRepository
```

## Dependency Injection

- **Always** use Hilt — never instantiate repositories, managers, or DAOs manually
- Inject via constructor (`@Inject constructor`)
- Use `@Singleton` for repositories and managers
- Use `@ActivityRetainedScoped` or `@ViewModelScoped` where appropriate

## Room

- DAOs return `Flow<T>` for observable queries
- Use `suspend fun` for one-shot write operations
- Complex queries use `@Query` with explicit SQL — avoid ORM magic
- Never access the database on the main thread
- Every schema change requires a migration — never use `fallbackToDestructiveMigration` in production

## Services

`BlockingService` runs as a foreground service. Keep it focused on monitoring — delegate business logic to domain managers.

## Screens

Each screen corresponds to one Composable function and one ViewModel:

```
presentation/ui/screens/profiles/
    ProfilesScreen.kt   ← @Composable, collects from ViewModel
    ProfilesViewModel.kt
    ProfilesUiState.kt  ← data class (can be nested in ViewModel)
```
