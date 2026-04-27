package com.rakshabandhan.sos.ui.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

data class ThemeModeState(
    val isDarkTheme: Boolean,
    val toggleTheme: () -> Unit
)

val LocalThemeMode = compositionLocalOf<ThemeModeState> {
    error("No ThemeModeState provided")
}

private val DarkColorScheme = darkColorScheme(
    primary          = Coral500,
    onPrimary        = Navy950,
    primaryContainer = Navy700,
    secondary        = Sky500,
    onSecondary      = Navy950,
    tertiary         = Mint500,
    onTertiary       = Navy950,
    background       = Navy950,
    onBackground     = Slate100,
    surface          = Navy900,
    onSurface        = Slate100,
    surfaceVariant   = Navy800,
    onSurfaceVariant = Slate200,
    error            = Coral600,
    onError          = Navy950,
    outline          = Slate700,
    outlineVariant   = Navy800
)

private val LightColorScheme = lightColorScheme(
    primary          = Coral500,
    onPrimary        = LightSurface,
    primaryContainer = Coral400,
    secondary        = Sky500,
    onSecondary      = LightSurface,
    tertiary         = Mint500,
    onTertiary       = LightSurface,
    background       = LightBackground,
    onBackground     = LightTextPrimary,
    surface          = LightSurface,
    onSurface        = LightTextPrimary,
    surfaceVariant   = LightSurfaceVariant,
    onSurfaceVariant = LightTextSecondary,
    error            = Coral600,
    onError          = LightSurface,
    outline          = LightOutline,
    outlineVariant   = LightOutlineVariant
)

@Composable
fun RakshaBandhanTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = Color.Transparent.toArgb()
            window.navigationBarColor = Color.Transparent.toArgb()
            WindowCompat.getInsetsController(window, view).apply {
                isAppearanceLightStatusBars = !darkTheme
                isAppearanceLightNavigationBars = !darkTheme
            }
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography  = AppTypography,
        shapes      = AppShapes,
        content     = content
    )
}
