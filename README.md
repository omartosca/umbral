# Umbral

> *"El umbral de tu casa es el punto de decisión consciente."*

**Umbral** es una app Android open-source que bloquea automáticamente apps de redes sociales cuando sales de casa, usando tags NFC como trigger físico.

---

## Concepto

El nombre "Umbral" proviene del concepto filosófico griego del **metaxy (μεταξύ)** - el espacio liminal entre dos estados. Representa el momento consciente de transición al cruzar el umbral de tu casa, donde eliges tu estado digital.

**¿Cómo funciona?**
1. Colocas un tag NFC barato ($1 USD) en tu puerta
2. Al salir de casa, tocas tu teléfono al tag
3. Umbral bloquea automáticamente tus apps de redes sociales
4. Al regresar, tocas el tag nuevamente para desbloquear

---

## Features (V1)

### Core
- **NFC tag reading/writing** — Compatible con NTAG213/215/216
- **App blocking** — Integración con UsageStatsManager
- **Whitelist** — Apps esenciales siempre accesibles (banco, sistema, etc.)
- **Multiple profiles** — Perfiles distintos para diferentes situaciones

### Advanced
- **Timer auto-unlock** — Desbloqueo automático después de X tiempo
- **QR alternative** — Fallback si NFC no disponible
- **Widgets** — 4 tipos: Status, Quick Toggle, Stats, Streak
- **Statistics** — Tiempo bloqueado, apps más bloqueadas, rachas con gráficas (Vico)
- **Physical unlock** — Solo el tag específico puede desbloquear (opcional)
- **Focus Mode** — Integración con Digital Wellbeing de Android
- **Quick Settings** — Toggle desde el panel rápido

### Extras
- **Gamification (Expedition)** — Sistema de logros, compañeros y progresión
- **Notifications module** — Seguimiento de notificaciones bloqueadas
- **Design System v2** — Componentes Material 3 personalizados con tema Umbral

---

## Tech Stack

| Capa | Tecnología |
|------|------------|
| Lenguaje | Kotlin 2.1 |
| UI | Jetpack Compose + Material Design 3 |
| Arquitectura | Clean Architecture + MVVM |
| Base de datos | Room 2.6 (SQLite) + DataStore |
| DI | Hilt 2.54 |
| Animaciones | Lottie 6.3 |
| Gráficas | Vico 2.0 |
| QR / Cámara | CameraX 1.4 + ML Kit Barcode |
| Widgets | Jetpack Glance 1.1 |
| Build | Gradle (Kotlin DSL) |
| Tests | JUnit + MockK + Turbine + Robolectric |

---

## Estado del Proyecto

**Fase actual:** Desarrollo completo — Beta

### Progreso
- [x] Inicialización del proyecto
- [x] Technical decisions documentadas
- [x] Arquitectura detallada
- [x] Análisis competitivo
- [x] Especificaciones por módulo
- [x] Plan de implementación
- [x] Desarrollo (todos los features V1 implementados)
- [x] Testing (150+ unit tests)
- [x] Design System v2
- [x] Gamification system (Expedition)
- [x] Firebase App Distribution configurado
- [ ] Release en Google Play Store
- [ ] Release en F-Droid

### Base de Datos
- **Room versión:** 6
- **Tablas:** 12 (perfiles, apps, NFC, sesiones, intentos, eventos, gamificación, notificaciones)
- **Migraciones:** v1 → v6 con scripts completos

---

## Build & Run

**Requisitos:**
- Android Studio Hedgehog o superior
- JDK 17
- Android SDK 35

```bash
git clone https://github.com/omartosca/umbral.git
cd umbral
./gradlew assembleDebug
```

Para tests:

```bash
./gradlew test
```

---

## Documentación

- [CONTRIBUTING.md](CONTRIBUTING.md) — Guía de contribución y workflow de branches
- [TESTS_IMPLEMENTED.md](TESTS_IMPLEMENTED.md) — Resumen de tests implementados
- [docs/README.md](docs/README.md) — Índice de documentación técnica completa
- [docs/reference/technical-decisions.md](docs/reference/technical-decisions.md) — Stack, arquitectura y decisiones
- [docs/reference/competitive-analysis.md](docs/reference/competitive-analysis.md) — Análisis de mercado
- [docs/reference/modules/](docs/reference/modules/) — Specs técnicas por módulo

---

## Inspiración y Colaboración

Umbral está inspirado en [**Foqos**](https://github.com/awaseem/foqos), una excelente app iOS open-source con funcionalidad similar.

**Estrategia:**
- Foqos cubre iOS perfectamente
- Umbral cubre Android
- Colaboración, no competencia
- Tags NFC compatibles entre ambas apps

---

## Diferenciadores

vs **Foqos** (iOS open source):
- Plataforma Android nativa
- UI en español para el mercado hispanohablante
- Sistema de gamificación integrado

vs **Brick** (iOS/Android comercial):
- 100% gratis y open source
- No requiere hardware propietario
- Tags NFC baratos (desde $1 USD)

vs **Unpluq** (iOS/Android comercial):
- Sin suscripción mensual
- Código abierto y auditable
- Privacidad total (100% local, sin cloud)

---

## Privacidad

- **100% local** — Sin backend en V1
- **Sin tracking** — Cero analytics por defecto
- **Open source** — Auditable por cualquiera
- **Sin permisos innecesarios** — Solo los estrictamente necesarios

---

## Distribución

- Google Play Store (primario) — *próximamente*
- F-Droid (secundario, usuarios privacy-focused) — *próximamente*

---

## Roadmap

### V1.0 — Core (completado)
Todos los features listados arriba implementados y testeados.

### V1.1 — Polish (en progreso)
- Bug fixes basados en feedback beta
- Mejoras de UX en onboarding
- Optimizaciones de rendimiento

### V2.0 — Cloud Features
- Backend Supabase (opcional, opt-in)
- Sync de perfiles entre dispositivos
- Multi-device support
- Tier premium

### V3.0 — Advanced
- Bloqueo de sitios web
- Triggers por geolocalización
- Bloqueo programado por horario
- Social features (accountability partner)

---

## Contribuciones

¡Contribuciones bienvenidas!

Lee [CONTRIBUTING.md](CONTRIBUTING.md) para el workflow de ramas, formato de commits y convenciones de código. En resumen:

- Forkea el repo y crea una rama `feature/nombre-feature`
- Escribe tests para tu código
- Abre un PR contra `develop`

---

## Licencia

MIT — ver [LICENSE](LICENSE)

---

## Agradecimientos

- [Foqos](https://github.com/awaseem/foqos) — Inspiración y referencia iOS
- Comunidad open source de Android
- Filósofos griegos por el concepto de metaxy

---

**Proyecto iniciado:** 2026-01-03
**Última actualización:** 2026-03-08
