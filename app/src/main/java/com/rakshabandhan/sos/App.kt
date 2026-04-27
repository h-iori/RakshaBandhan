package com.rakshabandhan.sos

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.rakshabandhan.sos.navigation.DemoApp
import com.rakshabandhan.sos.ui.theme.LocalThemeMode
import com.rakshabandhan.sos.ui.theme.RakshaBandhanTheme
import com.rakshabandhan.sos.ui.theme.ThemeModeState

@Composable
fun RakshaBandhanApp() {
    val systemTheme = isSystemInDarkTheme()
    var isDarkTheme by remember { mutableStateOf(systemTheme) }

    val themeModeState = remember(isDarkTheme) {
        ThemeModeState(
            isDarkTheme = isDarkTheme,
            toggleTheme = { isDarkTheme = !isDarkTheme }
        )
    }

    CompositionLocalProvider(LocalThemeMode provides themeModeState) {
        RakshaBandhanTheme(darkTheme = isDarkTheme) {
            DemoApp()
        }
    }
}