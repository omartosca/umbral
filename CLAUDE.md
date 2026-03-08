# Instrucciones de Proyecto - Umbral

## Información del Proyecto

**Nombre:** Umbral
**Descripción:** App Android open-source para bloqueo automático de apps mediante NFC tags
**Stack:** Kotlin + Jetpack Compose + Room + Hilt
**Arquitectura:** Clean Architecture + MVVM
**Metodología:** Oden (Documentation-First Development)
**Estado:** Desarrollo completo — fase Beta / distribución

---

## Naming Conventions CRÍTICAS

### Database
- **Nombres de tablas:** INGLÉS (ej: `blocking_profiles`, `nfc_tags`)
- **Nombres de columnas:** INGLÉS snake_case (ej: `created_at`, `is_whitelisted`)
- **NUNCA:** Nombres en español en DB

### Código Kotlin
- **Classes:** PascalCase en INGLÉS (ej: `BlockingProfile`, `NfcTagManager`)
- **Functions/Variables:** camelCase en INGLÉS (ej: `startBlocking`, `profileId`)
- **Packages:** lowercase en INGLÉS (ej: `com.umbral.nfc`, `com.umbral.blocking`)

### UI - Textos para Usuario
- **Todos los strings visibles:** ESPAÑOL
- **Usar strings.xml:** Siempre, nunca hardcodear texto
- **Ejemplos:**
  - ✅ `<string name="btn_save">Guardar</string>`
  - ✅ `<string name="profile_name">Nombre del perfil</string>`
  - ❌ Hardcoded: `Text("Save")` o `Text("Guardar")`

---

## Filosofía del Producto

> "Umbral" representa el concepto filosófico griego del **metaxy (μεταξύ)** - el espacio liminal entre dos estados. El momento consciente de transición al cruzar el umbral de tu casa.

**Principios:**
1. **Privacidad primero:** 100% local, sin cloud sync en V1
2. **Open source:** Todo el código disponible en GitHub
3. **UX simple:** Funcionar debe ser obvio, no requiere manual
4. **Respeto al usuario:** No dark patterns, fácil desinstalar si no funciona

---

## Decisiones Técnicas Clave

### 1. Android Only (por ahora)
- iOS ya está cubierto por Foqos (open source)
- Colaboración con Foqos, no competencia
- Mejor soporte NFC en Android que iOS

### 2. 100% Local-First
- Sin backend en V1
- Room Database para persistencia (versión 6)
- DataStore para preferences
- Funciona completamente offline

### 3. App Blocking Strategy
**Nivel 1 (Preferido):** UsageStatsManager
- Menos fricción en Google Play
- API oficial de Google

**Nivel 2 (Backup):** AccessibilityService
- Solo si necesario
- Requiere Permission Declaration Form
- Mayor escrutinio de Google

### 4. Build Config
- `compileSdk`: 35 (Android 15)
- `minSdk`: 26 (Android 8.0+)
- `targetSdk`: 35
- Kotlin: 2.1.0, AGP: 8.7.3, Java: 17

---

## Features V1 (Implementadas)

### Core ✅
- [x] NFC tag reading/writing (NTAG213/215/216)
- [x] Bloqueo básico de apps (UsageStatsManager)
- [x] Whitelist de apps esenciales
- [x] Multiple blocking profiles

### Advanced ✅
- [x] Timer-based auto-unblock
- [x] QR code alternative to NFC
- [x] Widgets (4 tipos: Status, Quick Toggle, Stats, Streak)
- [x] Usage statistics con gráficas (Vico)
- [x] Physical unlock requirement (optional)
- [x] Focus Mode integration
- [x] Shortcuts & Quick Settings tile
- [x] 6-step onboarding flow con permisos
- [x] Foreground service para monitoreo en background
- [x] Blocking overlay screen

### Extras ✅
- [x] Gamification system (Expedition) — logros, compañeros, progresión, mapa
- [x] Notifications module — seguimiento de notificaciones bloqueadas
- [x] Design System v2 — 20+ componentes Material 3 con tema Umbral
- [x] Blocking event log unificado

---

## Base de Datos (Room v6)

**Versión actual:** 6
**Schemas en:** `app/schemas/`
**Migraciones activas:** v2→3, v3→4, v4→5, v5→6

### Tablas

| Tabla | Propósito |
|-------|-----------|
| `blocking_profiles` | Perfiles de bloqueo del usuario |
| `blocked_apps` | Apps bloqueadas por perfil |
| `nfc_tags` | Tags NFC registrados |
| `blocking_sessions` | Sesiones de bloqueo activas |
| `blocked_attempts` | Log de intentos de acceso |
| `blocking_events` | Evento unificado (BLOCK_STARTED/ENDED, APP_ATTEMPT) |
| `companion` | Estado y evolución del compañero (gamificación) |
| `locations` | Mapa de ubicaciones (gamificación) |
| `progress` | Progresión del jugador (gamificación) |
| `achievements` | Logros desbloqueados (gamificación) |
| `decorations` | Decoraciones desbloqueadas (gamificación) |
| `blocked_notifications` | Notificaciones bloqueadas |

**Al agregar una migración:** incrementar la versión en `UmbralDatabase.kt` y añadir el script en `DatabaseMigrations.kt`.

---

## Estructura del Proyecto

```
umbral/
├── docs/
│   ├── guides/
│   ├── reference/
│   │   ├── technical-decisions.md
│   │   ├── competitive-analysis.md
│   │   ├── implementation-plan.md
│   │   ├── user-personas.md
│   │   ├── user-stories.md
│   │   └── modules/          # Specs por módulo
│   ├── development/
│   │   ├── current/          # Features en progreso
│   │   └── completed/        # Features completadas
│   └── archived/
├── .claude/
│   ├── epics/                # Tracking de épicas e issues
│   ├── prds/                 # Product Requirements Documents
│   └── rules/                # Reglas del proyecto para agentes
├── app/
│   ├── schemas/              # Room migration schemas (v1-v6)
│   └── src/main/java/com/umbral/
│       ├── data/             # DAOs, repositorios, entidades
│       ├── domain/           # Modelos e interfaces
│       ├── presentation/     # ViewModels, pantallas, componentes
│       ├── expedition/       # Módulo de gamificación
│       ├── glance/           # Widgets (Jetpack Glance)
│       ├── notifications/    # Módulo de notificaciones
│       └── di/               # Módulos Hilt
├── CLAUDE.md
├── AGENTS.md
├── CONTRIBUTING.md
├── README.md
└── TESTS_IMPLEMENTED.md
```

---

## Reglas de Documentación

### SIEMPRE documentar:
- Nuevas features o sistemas
- Cambios de arquitectura
- Migraciones de base de datos
- Decisiones de diseño importantes
- Guías de testing para features complejas

### NUNCA documentar:
- Bugfixes menores
- Cambios de estilo/UI simples
- Ajustes de configuración triviales

### Ubicación de archivos:
- **Features en desarrollo:** `docs/development/current/<feature-name>/`
- **Features completadas:** `docs/development/completed/`
- **Specs técnicas:** `docs/reference/modules/<module-name>.md`
- **Guías permanentes:** `docs/guides/`

---

## Git Workflow

### Commits
- **NO** incluir "Generated with Claude Code" ni "Co-Authored-By: Claude"
- **Formato:** `[Type] Brief description`
- **Tipos:** Feat / Fix / Docs / Refactor / Test / Chore

### Branches
- `main` — Producción estable (protegida, requiere PR + aprobación)
- `develop` — Desarrollo activo
- `feature/nombre` — Features individuales
- `claude/nombre` — Ramas generadas por agentes

---

## Comandos Oden Disponibles

### Durante Desarrollo
- `/oden:daily` - Registrar progreso diario
- `/oden:test` - Testing strategy
- `/oden:review` - Code review
- `/oden:debug` - Debugging

### Gestión
- `/oden:status` - Ver estado del proyecto
- `/oden:help` - Ver todos los comandos

---

## Estado Actual y Próximos Pasos

**Fase:** Beta — preparando release a Google Play y F-Droid

### Tests
- 150+ unit tests (MockK + Turbine + Robolectric)
- Tests de DAOs, repositorios, ViewModels, managers
- JaCoCo configurado para coverage

### CI/CD
- GitHub Actions: build + tests en push/PR a `main` y `develop`
- Firebase App Distribution configurado para builds de release

### Próximos pasos
1. Beta testing con usuarios reales
2. Correcciones basadas en feedback
3. Preparar listing de Google Play Store
4. Release en F-Droid

---

## Recursos de Referencia

- [Foqos GitHub](https://github.com/awaseem/foqos) — iOS reference
- [Android NFC Guide](https://developer.android.com/develop/connectivity/nfc/nfc)
- [Jetpack Compose](https://developer.android.com/jetpack/compose)
- [Clean Architecture](https://blog.cleancoder.com/uncle-bob/2012/08/13/the-clean-architecture.html)
- [Material Design 3](https://m3.material.io/)

---

**Creado:** 2026-01-03
**Última actualización:** 2026-03-08
