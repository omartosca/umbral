# Naming Conventions

## Kotlin Code — English only

- **Classes:** `PascalCase` → `BlockingProfile`, `NfcTagManager`, `ProfileRepository`
- **Functions & variables:** `camelCase` → `startBlocking`, `profileId`, `isStrictMode`
- **Constants:** `SCREAMING_SNAKE_CASE` → `MAX_BLOCKED_APPS`, `NFC_TAG_UID`
- **Packages:** `lowercase` → `com.umbral.nfc`, `com.umbral.blocking`, `com.umbral.data`

## Database — English only

- **Table names:** `snake_case` → `blocking_profiles`, `nfc_tags`, `blocking_events`
- **Column names:** `snake_case` → `created_at`, `is_whitelisted`, `profile_id`
- **NEVER** use Spanish in database identifiers

## UI Strings — Spanish only

All user-visible text must be in Spanish via `strings.xml`. Never hardcode text.

```xml
<!-- ✅ Correct -->
<string name="btn_save">Guardar</string>
<string name="profile_name">Nombre del perfil</string>
<string name="error_nfc_not_supported">Tu dispositivo no soporta NFC</string>

<!-- ❌ Wrong -->
<!-- Text("Guardar") — hardcoded -->
<!-- Text("Save") — wrong language + hardcoded -->
```

In Compose: always use `stringResource(R.string.key)`.

## Files

- **Kotlin files:** match the primary class name → `BlockingProfile.kt`, `NfcTagDao.kt`
- **Layout/resource files:** `snake_case` → `activity_main.xml`, `ic_nfc_tag.xml`
- **Test files:** mirror the tested class → `BlockingProfileRepositoryTest.kt`

## Hilt Modules

- Suffix with `Module` → `DatabaseModule`, `RepositoryModule`, `ManagerModule`
- Bind interfaces with `@Binds` in abstract modules, provide instances with `@Provides`
