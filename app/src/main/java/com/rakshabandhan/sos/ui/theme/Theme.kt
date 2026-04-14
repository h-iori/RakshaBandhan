package com.rakshabandhan.sos.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable

private val AppColorScheme = darkColorScheme(
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

@Composable
fun RakshaBandhanTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = AppColorScheme,
        typography  = AppTypography,
        shapes      = AppShapes,
        content     = content
    )
}
