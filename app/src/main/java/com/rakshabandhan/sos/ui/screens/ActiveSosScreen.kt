package com.rakshabandhan.sos.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CallEnd
import androidx.compose.material.icons.filled.ExpandCircleDown
import androidx.compose.material.icons.filled.Timer
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
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.rakshabandhan.sos.model.ResponderState
import com.rakshabandhan.sos.model.SosState
import com.rakshabandhan.sos.model.demoResponders
import com.rakshabandhan.sos.model.demoTimeline
import com.rakshabandhan.sos.ui.components.DemoFrame
import com.rakshabandhan.sos.ui.components.HeroMetric
import com.rakshabandhan.sos.ui.components.LinearMetricRow
import com.rakshabandhan.sos.ui.components.MapPlaceholderCard
import com.rakshabandhan.sos.ui.components.ResponderCard
import com.rakshabandhan.sos.ui.components.StatusChip
import com.rakshabandhan.sos.ui.components.TimelineCard
import com.rakshabandhan.sos.ui.haptics.AppHapticEvent
import com.rakshabandhan.sos.ui.haptics.withHaptic
import com.rakshabandhan.sos.ui.theme.Amber500
import com.rakshabandhan.sos.ui.theme.Coral500
import com.rakshabandhan.sos.ui.theme.Mint500
import com.rakshabandhan.sos.ui.theme.Slate100
import com.rakshabandhan.sos.ui.theme.Slate200
import kotlinx.coroutines.delay

@Composable
fun ActiveSosScreen(onStop: () -> Unit, onExtend: () -> Unit) {
    // Live counting timer starting at 08:42
    var elapsedSeconds by remember { mutableIntStateOf(8 * 60 + 42) }
    var showStopConfirmation by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) {
        while (true) { delay(1000L); elapsedSeconds++ }
    }
    val minutes = elapsedSeconds / 60
    val seconds = elapsedSeconds % 60
    val timerDisplay = "%02d:%02d".format(minutes, seconds)
    val activeResponders = remember { demoResponders.filter { it.state != ResponderState.NONE } }
    val helpersOnTheWay = activeResponders.count { it.state == ResponderState.COMING }
    val helpersArrived = activeResponders.count { it.state == ResponderState.ARRIVED }

    // Staggered entrance
    val visible = remember { Array(7) { mutableStateOf(false) } }
    LaunchedEffect(Unit) {
        visible.forEachIndexed { i, s -> delay(i * 80L); s.value = true }
    }

    @Composable
    fun Slot(i: Int, content: @Composable () -> Unit) {
        AnimatedVisibility(
            visible = visible[i].value,
            enter = slideInVertically(
                initialOffsetY = { it / 2 },
                animationSpec = spring(Spring.DampingRatioMediumBouncy, Spring.StiffnessMedium)
            ) + fadeIn(tween(240))
        ) { content() }
    }

    DemoFrame(
        title = "Active SOS",
        subtitle = "",
        trailing = { StatusChip("ACTIVE", SosState.ACTIVE) }
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {

            Slot(0) {
                Row(horizontalArrangement = Arrangement.spacedBy(12.dp), modifier = Modifier.fillMaxWidth()) {
                    HeroMetric(timerDisplay, "Elapsed time", modifier = Modifier.weight(1f), accentColor = Coral500)
                    HeroMetric("500m", "Broadcast radius", modifier = Modifier.weight(1f), accentColor = Amber500)
                }
            }

            Slot(1) {
                MapPlaceholderCard(
                    title = "Nearby users",
                    subtitle = "Nearby helpers around your location. Tap to expand.",
                    showRoute = true,
                    compactFullscreenFooter = true,
                    fullscreenStatusLabel = "Active",
                    footerText = null
                )
            }

            Slot(2) {
                Row(horizontalArrangement = Arrangement.spacedBy(12.dp), modifier = Modifier.fillMaxWidth()) {
                    HeroMetric(helpersOnTheWay.toString(), "On the way", modifier = Modifier.weight(1f), accentColor = Mint500)
                    HeroMetric(helpersArrived.toString(), "Arrived", modifier = Modifier.weight(1f), accentColor = Mint500)
                }
            }

            Slot(3) {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    LinearMetricRow("Broadcast status", "Active within 500m")
                }
            }

            Slot(4) {
                Row(horizontalArrangement = Arrangement.spacedBy(12.dp), modifier = Modifier.fillMaxWidth()) {
                    Button(
                        onClick = withHaptic(AppHapticEvent.TAP) { showStopConfirmation = true },
                        modifier = Modifier.weight(1f).height(52.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Coral500, contentColor = Slate100)
                    ) {
                        Icon(Icons.Filled.CallEnd, null, modifier = Modifier.size(18.dp))
                        Spacer(Modifier.width(6.dp))
                        Text("Stop SOS")
                    }
                    Button(
                        onClick = withHaptic(AppHapticEvent.TAP, onExtend),
                        modifier = Modifier.weight(1f).height(52.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Mint500, contentColor = Slate100)
                    ) {
                        Icon(Icons.Filled.ExpandCircleDown, null, modifier = Modifier.size(18.dp))
                        Spacer(Modifier.width(6.dp))
                        Text("Extend Broadcast")
                    }
                }
            }

            Slot(5) {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text("Nearby helpers", style = MaterialTheme.typography.titleMedium, color = Slate100, fontWeight = FontWeight.SemiBold)
                    activeResponders.forEachIndexed { i, r -> ResponderCard(r, animIndex = i) }
                }
            }

            Slot(6) {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text("Event timeline", style = MaterialTheme.typography.titleMedium, color = Slate100, fontWeight = FontWeight.SemiBold)
                    demoTimeline.forEachIndexed { i, e -> TimelineCard(e.time, e.label, e.detail, animIndex = i) }
                }
            }
        }
    }

    if (showStopConfirmation) {
        AlertDialog(
            onDismissRequest = { showStopConfirmation = false },
            title = { Text("Stop SOS?") },
            text = { Text("Only stop the alert if you are safe. This will end live location sharing for nearby users.") },
            confirmButton = {
                TextButton(
                    onClick = withHaptic(AppHapticEvent.CONFIRM) {
                        showStopConfirmation = false
                        onStop()
                    }
                ) {
                    Text("Yes, Stop")
                }
            },
            dismissButton = {
                TextButton(onClick = withHaptic(AppHapticEvent.REJECT) { showStopConfirmation = false }) {
                    Text("Keep Active")
                }
            }
        )
    }
}
