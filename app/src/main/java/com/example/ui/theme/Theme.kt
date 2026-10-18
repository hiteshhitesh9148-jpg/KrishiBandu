package com.example.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val LightColorScheme = lightColorScheme(
    primary = AgriPrimary,
    onPrimary = AgriOnPrimary,
    primaryContainer = AgriPrimaryContainer,
    onPrimaryContainer = AgriOnPrimaryContainer,
    inversePrimary = AgriPrimaryFixedDim,
    secondary = AgriSecondary,
    onSecondary = AgriOnSecondary,
    secondaryContainer = AgriSecondaryContainer,
    onSecondaryContainer = AgriOnSecondaryContainer,
    tertiary = AgriTertiary,
    onTertiary = AgriOnTertiary,
    tertiaryContainer = AgriTertiaryContainer,
    onTertiaryContainer = AgriOnTertiaryContainer,
    background = AgriBackground,
    onBackground = AgriOnBackground,
    surface = AgriSurface,
    onSurface = AgriOnSurface,
    surfaceVariant = AgriSurfaceContainerHighest,
    onSurfaceVariant = AgriOnSurfaceVariant,
    surfaceContainerLowest = AgriSurfaceContainerLowest,
    surfaceContainerLow = AgriSurfaceContainerLow,
    surfaceContainer = AgriSurfaceContainer,
    surfaceContainerHigh = AgriSurfaceContainerHigh,
    surfaceContainerHighest = AgriSurfaceContainerHighest,
    outline = AgriOutline,
    outlineVariant = AgriOutlineVariant,
    error = AgriError,
    onError = AgriOnError,
    errorContainer = AgriErrorContainer,
    onErrorContainer = AgriOnErrorContainer
)

private val DarkColorScheme = darkColorScheme(
    primary = AgriPrimaryFixedDim,
    onPrimary = AgriOnPrimaryFixed,
    primaryContainer = AgriPrimaryContainer,
    onPrimaryContainer = AgriOnPrimaryContainer,
    secondary = AgriSecondaryFixedDim,
    onSecondary = AgriOnSecondary,
    secondaryContainer = AgriSecondary,
    onSecondaryContainer = AgriSecondaryFixed,
    tertiary = AgriTertiaryFixedDim,
    onTertiary = AgriOnTertiary,
    tertiaryContainer = AgriTertiaryContainer,
    onTertiaryContainer = AgriTertiaryFixed,
    background = AgriOnSurface,
    onBackground = AgriSurface,
    surface = AgriOnSurface,
    onSurface = AgriSurface,
    surfaceVariant = AgriOnSurfaceVariant,
    onSurfaceVariant = AgriSurfaceContainerHighest,
    outline = AgriOutlineVariant,
    outlineVariant = AgriOutline,
    error = AgriError,
    onError = AgriOnError
)

@Composable
fun KrishiBandhuTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false, // Keep authentic agricultural theme
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as? Activity)?.window
            if (window != null) {
                window.statusBarColor = colorScheme.surface.toArgb()
                WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme
            }
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
