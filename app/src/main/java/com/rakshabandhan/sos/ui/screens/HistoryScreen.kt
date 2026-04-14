package com.rakshabandhan.sos.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.History
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.rakshabandhan.sos.model.SosIncident
import com.rakshabandhan.sos.model.SosState
import com.rakshabandhan.sos.ui.components.DemoFrame
import com.rakshabandhan.sos.ui.components.LinearMetricRow
import com.rakshabandhan.sos.ui.components.StatusChip
import com.rakshabandhan.sos.ui.components.TimelineCard
import com.rakshabandhan.sos.ui.theme.Amber500
import com.rakshabandhan.sos.ui.theme.Mint500
import com.rakshabandhan.sos.ui.theme.Slate100
import com.rakshabandhan.sos.ui.theme.Slate200
import kotlinx.coroutines.delay

@Composable
fun HistoryScreen(incidents: List<SosIncident>) {
    // Top stats entrance
    var statsVisible by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) { delay(60); statsVisible = true }

    DemoFrame(
        title = "Incident history",
        subtitle = "Your past SOS events.",
        trailing = { Icon(Icons.Filled.History, null, tint = Amber500) }
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {

            AnimatedVisibility(
                visible = statsVisible,
                enter = fadeIn(tween(350)) + slideInVertically(initialOffsetY = { it / 3 })
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.fillMaxWidth()) {
                        com.rakshabandhan.sos.ui.components.HeroMetric(
                            incidents.size.toString(), "Total incidents",
                            modifier = Modifier.weight(1f), accentColor = Amber500
                        )
                        com.rakshabandhan.sos.ui.components.HeroMetric(
                            incidents.count { it.state == SosState.STOPPED }.toString(), "Resolved",
                            modifier = Modifier.weight(1f), accentColor = Mint500
                        )
                    }
                    LinearMetricRow("Auto-stop events", "1")
                    LinearMetricRow("Total responders engaged", incidents.sumOf { it.responderCount }.toString())
                }
            }

            // Staggered incident cards
            incidents.forEachIndexed { i, incident ->
                var cardVisible by remember { mutableStateOf(false) }
                LaunchedEffect(incident.id) { delay(100L + i * 120L); cardVisible = true }
                AnimatedVisibility(
                    visible = cardVisible,
                    enter = slideInVertically(
                        initialOffsetY = { it },
                        animationSpec = spring(Spring.DampingRatioMediumBouncy, Spring.StiffnessMediumLow)
                    ) + fadeIn(tween(280))
                ) {
                    IncidentCard(incident)
                }
            }

            // Detail panel
            var detailVisible by remember { mutableStateOf(false) }
            LaunchedEffect(Unit) { delay(500); detailVisible = true }
            AnimatedVisibility(visible = detailVisible, enter = fadeIn(tween(400))) {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text("Selected incident detail", style = MaterialTheme.typography.titleMedium, color = Slate100, fontWeight = FontWeight.SemiBold)
                    DetailPanel()
                }
            }
        }
    }
}

@Composable
private fun IncidentCard(incident: SosIncident) {
    ElevatedCard(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.elevatedCardColors(containerColor = Color(0xFF111F38)),
        shape = MaterialTheme.shapes.medium
    ) {
        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(incident.title, style = MaterialTheme.typography.titleMedium, color = Slate100, fontWeight = FontWeight.SemiBold)
                    Text(incident.address, style = MaterialTheme.typography.bodySmall, color = Slate200)
                }
                StatusChip(
                    text = when (incident.state) {
                        SosState.ACTIVE -> "ACTIVE"; SosState.ENDING -> "ENDING"; SosState.STOPPED -> "STOPPED"
                    },
                    state = incident.state
                )
            }
            Text("${incident.startedAt} · ${incident.duration}", style = MaterialTheme.typography.bodySmall, color = Slate200)
            Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
                Text("${incident.responderCount} responders", style = MaterialTheme.typography.labelSmall, color = Slate100)
                Text("${incident.arrivedCount} arrived", style = MaterialTheme.typography.labelSmall, color = Mint500)
            }
            Text(incident.note, style = MaterialTheme.typography.bodySmall, color = Slate200)
        }
    }
}

@Composable
private fun DetailPanel() {
    ElevatedCard(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.elevatedCardColors(containerColor = Color(0xFF111F38)),
        shape = MaterialTheme.shapes.medium
    ) {
        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
            Text("SOS-2048", style = MaterialTheme.typography.titleLarge, color = Slate100, fontWeight = FontWeight.Bold)
            Text(
                "Full incident breakdown with timeline for audit and outcome tracking.",
                style = MaterialTheme.typography.bodySmall, color = Slate200
            )
            TimelineCard("10:14 PM", "Broadcast started", "Nearby users notified within 500m radius.", animIndex = 0)
            TimelineCard("10:16 PM", "Rescuer marked coming", "Aarav started navigation to the scene.", animIndex = 1)
            TimelineCard("10:18 PM", "Scene reached", "Nikhil arrival logged for audit.", animIndex = 2)
        }
    }
}