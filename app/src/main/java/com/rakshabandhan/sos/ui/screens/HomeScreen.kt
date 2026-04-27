package com.rakshabandhan.sos.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.rakshabandhan.sos.model.demoIncidents
import com.rakshabandhan.sos.ui.haptics.AppHapticEvent
import com.rakshabandhan.sos.ui.haptics.withHaptic
import com.rakshabandhan.sos.ui.components.DemoFrame
import com.rakshabandhan.sos.ui.components.HeroMetric
import com.rakshabandhan.sos.ui.components.LinearMetricRow
import com.rakshabandhan.sos.ui.components.MapPlaceholderCard
import com.rakshabandhan.sos.ui.components.PrimarySosButton
import com.rakshabandhan.sos.ui.components.SecondaryActionButton
import com.rakshabandhan.sos.ui.theme.Coral500
import com.rakshabandhan.sos.ui.theme.Mint500
import com.rakshabandhan.sos.ui.theme.Sky500
import kotlinx.coroutines.delay

@Composable
fun HomeScreen(onGoToSos: () -> Unit) {
    var showConfirm by remember { mutableStateOf(false) }

    // Staggered card entrance
    val visible = remember { Array(6) { mutableStateOf(false) } }
    LaunchedEffect(Unit) {
        visible.forEachIndexed { i, state ->
            delay(i * 90L)
            state.value = true
        }
    }

    @Composable
    fun Slot(index: Int, content: @Composable () -> Unit) {
        AnimatedVisibility(
            visible = visible[index].value,
            enter = slideInVertically(
                initialOffsetY = { it / 2 },
                animationSpec = spring(Spring.DampingRatioMediumBouncy, Spring.StiffnessMedium)
            ) + fadeIn(tween(250))
        ) { content() }
    }

    DemoFrame(
        title = "Emergency SOS",
        subtitle = "One tap sends your live location within 500m.",
        trailing = { Icon(Icons.Filled.Shield, null, tint = Mint500) }
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {

            Slot(0) {
                HeroMetric("500m", "Broadcast radius", modifier = Modifier.fillMaxWidth(), accentColor = Coral500)
            }

            Slot(1) {
                MapPlaceholderCard(
                    title = "Up to 500m from your location",
                    subtitle = "Tap to expand the 500m radius.",
                    showRoute = false,
                    compactFullscreenFooter = true,
                    fullscreenStatusLabel = "Inactive"
                )
            }

            Slot(2) { PrimarySosButton(onClick = { showConfirm = true }) }

            Slot(3) {
                HeroMetric("15 min", "Auto stop", modifier = Modifier.fillMaxWidth(), accentColor = Sky500)
            }

            Slot(4) {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    LinearMetricRow("Recent incidents", "${demoIncidents.size} entries")
                }
            }

            Slot(5) {
                SecondaryActionButton(label = "View history", onClick = {}, modifier = Modifier.fillMaxWidth())
            }
        }
    }

    if (showConfirm) {
        AlertDialog(
            onDismissRequest = { showConfirm = false },
            confirmButton = {
                Button(
                    onClick = withHaptic(AppHapticEvent.CONFIRM) { showConfirm = false; onGoToSos() },
                    colors = ButtonDefaults.buttonColors(containerColor = Coral500, contentColor = MaterialTheme.colorScheme.onPrimary)
                ) { Text("Confirm SOS") }
            },
            dismissButton = { TextButton(onClick = withHaptic(AppHapticEvent.REJECT) { showConfirm = false }) { Text("Cancel") } },
            icon = { Icon(Icons.Filled.Bolt, null, tint = Coral500) },
            title = { Text("Confirm emergency broadcast") },
            text = {
                Text(
                    "Nearby users within 500m will receive the alert with your live location.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        )
    }
}
