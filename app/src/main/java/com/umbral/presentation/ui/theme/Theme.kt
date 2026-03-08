package com.umbral.presentation.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

/**
 * Umbral Design System 2.0 - Theme
 *
 * Sage Teal accent colors with optimized backgrounds:
 * - Light theme: Soft gray backgrounds with pure white surfaces
 * - Dark theme: Deep OLED-optimized blacks with subtle elevation
 *
 * Supports Dynamic Color (Material You) on Android 12+.
 * Falls back to Umbral sage teal palette on older devices.
 */

// =============================================================================
// LIGHT COLOR SCHEME
// =============================================================================
private val LightColorScheme = lightColorScheme(
    // Primary colors (sage teal accent)
    primary = LightAccentPrimary,
    onPrimary = Color.White,
    primaryContainer = LightAccentHover,
    onPrimaryContainer = LightTextPrimary,

    // Secondary colors
    secondary = LightAccentHover,
    onSecondary = LightTextPrimary,
    secondaryContainer = LightBackgroundSurface,
    onSecondaryContainer = LightTextPrimary,

    // Tertiary (info)
    tertiary = LightInfo,
    onTertiary = Color.White,
    tertiaryContainer = LightBackgroundSurface,
    onTertiaryContainer = LightInfo,

    // Background & Surface
    background = LightBackgroundBase,
    onBackground = LightTextPrimary,
    surface = LightBackgroundSurface,
    onSurface = LightTextPrimary,
    surfaceVariant = LightBackgroundElevated,
    onSurfaceVariant = LightTextSecondary,

    // Surface containers (Material 3 elevation system)
    surfaceContainer = LightBackgroundSurface,
    surfaceContainerLow = LightBackgroundBase,
    surfaceContainerHigh = LightBackgroundElevated,
    surfaceContainerHighest = LightBackgroundElevated,
    surfaceBright = LightBackgroundSurface,
    surfaceDim = LightBackgroundBase,

    // Outline
    outline = LightBorderDefault,
    outlineVariant = LightBorderFocus,

    // Error
    error = LightError,
    onError = Color.White,
    errorContainer = LightError.copy(alpha = 0.12f),
    onErrorContainer = LightError,

    // Scrim
    scrim = ScrimLight,

    // Inverse (for snackbars, etc.)
    inverseSurface = DarkBackgroundSurface,
    inverseOnSurface = DarkTextPrimary,
    inversePrimary = DarkAccentPrimary
)

// =============================================================================
// DARK COLOR SCHEME
// =============================================================================
private val DarkColorScheme = darkColorScheme(
    // Primary colors (sage teal accent)
    primary = DarkAccentPrimary,
    onPrimary = DarkBackgroundBase,
    primaryContainer = DarkAccentPressed,
    onPrimaryContainer = DarkTextPrimary,

    // Secondary colors
    secondary = DarkAccentHover,
    onSecondary = DarkBackgroundBase,
    secondaryContainer = DarkBackgroundElevated,
    onSecondaryContainer = DarkTextPrimary,

    // Tertiary (info)
    tertiary = DarkInfo,
    onTertiary = DarkBackgroundBase,
    tertiaryContainer = DarkBackgroundElevated,
    onTertiaryContainer = DarkInfo,

    // Background & Surface
    background = DarkBackgroundBase,
    onBackground = DarkTextPrimary,
    surface = DarkBackgroundSurface,
    onSurface = DarkTextPrimary,
    surfaceVariant = DarkBackgroundElevated,
    onSurfaceVariant = DarkTextSecondary,

    // Surface containers (Material 3 tonal elevation - brighter = higher)
    surfaceContainer = DarkBackgroundSurface,
    surfaceContainerLow = DarkBackgroundBase,
    surfaceContainerHigh = DarkBackgroundElevated,
    surfaceContainerHighest = DarkBackgroundElevated,
    surfaceBright = DarkBackgroundElevated,
    surfaceDim = DarkBackgroundBase,

    // Outline
    outline = DarkBorderDefault,
    outlineVariant = DarkBorderFocus,

    // Error
    error = DarkError,
    onError = DarkBackgroundBase,
    errorContainer = DarkError.copy(alpha = 0.12f),
    onErrorContainer = DarkError,

    // Scrim
    scrim = ScrimDark,

    // Inverse
    inverseSurface = LightBackgroundSurface,
    inverseOnSurface = LightTextPrimary,
    inversePrimary = LightAccentPrimary
)

// =============================================================================
// THEME COMPOSABLE
// =============================================================================

/**
 * Umbral app theme.
 *
 * @param darkTheme Whether to use dark theme. Defaults to system setting.
 * @param dynamicColor Whether to use Dynamic Color (Material You) on Android 12+.
 *   Falls back to the Umbral sage teal palette on older devices.
 * @param content The composable content to theme.
 */
@Composable
fun UmbralTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val context = LocalContext.current
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            if (darkTheme) dynamicDarkColorScheme(context)
            else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    // Update system bars color
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window

            // Use transparent system bars — let the app content handle color
            window.statusBarColor = Color.Transparent.toArgb()
            window.navigationBarColor = colorScheme.background.toArgb()

            // Icon colors (light icons on dark bg, dark icons on light bg)
            WindowCompat.getInsetsController(window, view).apply {
                isAppearanceLightStatusBars = !darkTheme
                isAppearanceLightNavigationBars = !darkTheme
            }
        }
    }

    // Provide spacing through CompositionLocal
    CompositionLocalProvider(
        LocalUmbralSpacing provides UmbralSpacingValues()
    ) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = UmbralTypography,
            shapes = UmbralShapes,
            content = content
        )
    }
}

// =============================================================================
// THEME EXTENSIONS
// =============================================================================

/**
 * Check if current theme is dark
 */
@Composable
fun isUmbralDarkTheme(): Boolean = isSystemInDarkTheme()

/**
 * Get current color scheme based on theme
 */
@Composable
fun umbralColorScheme() = if (isSystemInDarkTheme()) DarkColorScheme else LightColorScheme
