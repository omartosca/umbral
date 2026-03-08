# Testing Standards

## Stack

| Tool | Purpose |
|------|---------|
| JUnit 4 | Test runner and assertions |
| MockK | Mocking Kotlin classes and coroutines |
| Turbine | Testing `Flow` emissions |
| Robolectric | Android framework in unit tests (no device needed) |
| JaCoCo | Code coverage (configured in `app/jacoco.gradle`) |

## File Locations

```
app/src/test/java/com/umbral/          ← unit tests (JVM + Robolectric)
app/src/androidTest/java/com/umbral/   ← instrumented tests (device/emulator)
```

Mirror the main source structure:
- `data/local/dao/` tests go in `test/data/local/dao/`
- `presentation/viewmodel/` tests go in `test/presentation/viewmodel/`

## Coverage Expectations

- **Every DAO** must have integration tests (use `@RunWith(RobolectricTestRunner::class)` with an in-memory Room DB)
- **Every ViewModel** must have state-transition tests
- **Repositories** must test happy path + error cases
- **Managers** (NfcManager, BlockingManager, etc.) must test core logic

## Writing Unit Tests

### ViewModel tests

```kotlin
@Test
fun `activating a profile updates active state`() = runTest {
    val repo = mockk<ProfileRepository>()
    every { repo.getProfiles() } returns flowOf(listOf(fakeProfile))
    coEvery { repo.activateProfile(any()) } just Runs

    val viewModel = ProfilesViewModel(repo)
    viewModel.activateProfile("profile-1")

    viewModel.uiState.test {
        val state = awaitItem()
        assertEquals("profile-1", state.activeProfileId)
    }
}
```

### Flow tests (Turbine)

```kotlin
viewModel.uiState.test {
    assertEquals(HomeUiState(), awaitItem())          // initial state
    viewModel.loadData()
    val loaded = awaitItem()
    assertFalse(loaded.isLoading)
    cancelAndIgnoreRemainingEvents()
}
```

### DAO integration tests

```kotlin
@RunWith(RobolectricTestRunner::class)
class BlockingProfileDaoTest {
    private lateinit var db: UmbralDatabase
    private lateinit var dao: BlockingProfileDao

    @Before
    fun setup() {
        db = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            UmbralDatabase::class.java
        ).allowMainThreadQueries().build()
        dao = db.blockingProfileDao()
    }

    @After
    fun teardown() = db.close()

    @Test
    fun `insert and retrieve profile`() = runTest {
        dao.insert(fakeProfileEntity)
        val result = dao.getAll().first()
        assertEquals(1, result.size)
    }
}
```

### Mocking with MockK

```kotlin
val repo = mockk<ProfileRepository>()
every { repo.getProfiles() } returns flowOf(emptyList())
coEvery { repo.saveProfile(any()) } just Runs
verify { repo.getProfiles() }
coVerify { repo.saveProfile(any()) }
```

## What to Test

- State transitions in ViewModels (loading → success → error)
- Repository methods: correct DAO calls, correct mapping from entity to domain model
- Manager logic: blocking conditions, NFC tag validation, timer behavior
- Edge cases: empty lists, null values, permission denied scenarios

## What Not to Test

- Compose UI layout (covered by screenshot tests if needed)
- Third-party library internals (Room, Hilt, etc.)
- Android system behavior (NFC hardware, UsageStats APIs) — mock these

## Running Tests

```bash
# All unit tests
./gradlew test

# With coverage report
./gradlew jacocoTestReport

# Specific test class
./gradlew test --tests "com.umbral.presentation.viewmodel.HomeViewModelTest"
```
