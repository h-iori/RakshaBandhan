package com.rakshabandhan.sos.ui.haptics

import android.os.Build
import android.view.HapticFeedbackConstants
import android.view.View
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.semantics.Role

enum class AppHapticEvent {
    TAP,
    SELECTION,
    CONFIRM,
    REJECT,
    HEAVY_CLICK,
    LONG_PRESS
}

interface AppHaptics {
    fun perform(event: AppHapticEvent)
}

class AndroidAppHaptics(private val view: View) : AppHaptics {
    override fun perform(event: AppHapticEvent) {
        val constant = when (event) {
            AppHapticEvent.TAP -> HapticFeedbackConstants.VIRTUAL_KEY
            AppHapticEvent.SELECTION -> {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O_MR1) {
                    HapticFeedbackConstants.TEXT_HANDLE_MOVE
                } else {
                    HapticFeedbackConstants.KEYBOARD_TAP
                }
            }
            AppHapticEvent.CONFIRM -> {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                    HapticFeedbackConstants.CONFIRM
                } else {
                    HapticFeedbackConstants.VIRTUAL_KEY
                }
            }
            AppHapticEvent.REJECT -> {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                    HapticFeedbackConstants.REJECT
                } else {
                    HapticFeedbackConstants.LONG_PRESS
                }
            }
            AppHapticEvent.HEAVY_CLICK -> {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O_MR1) {
                    HapticFeedbackConstants.VIRTUAL_KEY
                } else {
                    HapticFeedbackConstants.LONG_PRESS
                }
            }
            AppHapticEvent.LONG_PRESS -> HapticFeedbackConstants.LONG_PRESS
        }
        view.performHapticFeedback(constant)
    }
}

val LocalAppHaptics = staticCompositionLocalOf<AppHaptics> {
    error("No AppHaptics provided")
}

@Composable
fun rememberAppHaptics(): AppHaptics {
    val view = LocalView.current
    return remember(view) { AndroidAppHaptics(view) }
}

@Composable
fun withHaptic(
    event: AppHapticEvent = AppHapticEvent.TAP,
    action: () -> Unit
): () -> Unit {
    val haptics = LocalAppHaptics.current
    return {
        haptics.perform(event)
        action()
    }
}

fun Modifier.hapticClickable(
    enabled: Boolean = true,
    onClickLabel: String? = null,
    role: Role? = null,
    hapticEvent: AppHapticEvent = AppHapticEvent.TAP,
    onClick: () -> Unit
): Modifier = composed {
    val haptics = LocalAppHaptics.current
    this.clickable(
        enabled = enabled,
        onClickLabel = onClickLabel,
        role = role,
        onClick = {
            haptics.perform(hapticEvent)
            onClick()
        }
    )
}

fun Modifier.hapticClickable(
    interactionSource: MutableInteractionSource,
    indication: androidx.compose.foundation.Indication?,
    enabled: Boolean = true,
    onClickLabel: String? = null,
    role: Role? = null,
    hapticEvent: AppHapticEvent = AppHapticEvent.TAP,
    onClick: () -> Unit
): Modifier = composed {
    val haptics = LocalAppHaptics.current
    this.clickable(
        interactionSource = interactionSource,
        indication = indication,
        enabled = enabled,
        onClickLabel = onClickLabel,
        role = role,
        onClick = {
            haptics.perform(hapticEvent)
            onClick()
        }
    )
}
