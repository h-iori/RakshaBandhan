package com.rakshabandhan.sos

import androidx.compose.runtime.Composable
import com.rakshabandhan.sos.navigation.DemoApp
import com.rakshabandhan.sos.ui.theme.RakshaBandhanTheme

@Composable
fun RakshaBandhanApp() {
    RakshaBandhanTheme {
        DemoApp()
    }
}