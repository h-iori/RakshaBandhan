package com.rakshabandhan.sos

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.CompositionLocalProvider
import com.rakshabandhan.sos.ui.haptics.LocalAppHaptics
import com.rakshabandhan.sos.ui.haptics.rememberAppHaptics

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        setContent {
            CompositionLocalProvider(
                LocalAppHaptics provides rememberAppHaptics()
            ) {
                RakshaBandhanApp()
            }
        }
    }
}