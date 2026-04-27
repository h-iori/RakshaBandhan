package com.rakshabandhan.sos.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import com.rakshabandhan.sos.ui.haptics.AppHapticEvent
import com.rakshabandhan.sos.ui.haptics.withHaptic
import com.rakshabandhan.sos.ui.haptics.hapticClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.DirectionsWalk
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.MyLocation
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.rakshabandhan.sos.model.IncidentSeverity
import com.rakshabandhan.sos.model.ResponderState
import com.rakshabandhan.sos.model.SosIncident
import com.rakshabandhan.sos.model.SosState
import com.rakshabandhan.sos.model.demoIncomingAlerts
import com.rakshabandhan.sos.ui.components.HeroMetric
import com.rakshabandhan.sos.ui.components.MapFooterStat
import com.rakshabandhan.sos.ui.components.MapPlaceholderCard
import com.rakshabandhan.sos.ui.components.ResponderCard
import com.rakshabandhan.sos.ui.components.TimelineCard
import com.rakshabandhan.sos.ui.theme.Amber500
import com.rakshabandhan.sos.ui.theme.Coral500
import com.rakshabandhan.sos.ui.theme.Mint500
import com.rakshabandhan.sos.ui.theme.Sky500
import java.util.Locale

@Composable
fun ResponderAlertScreen() {
    val alerts = remember { demoIncomingAlerts.filter { it.state != SosState.STOPPED } }
    var selectedAlertId by rememberSaveable { mutableStateOf<String?>(null) }

    BackHandler(enabled = selectedAlertId != null) {
        selectedAlertId = null
    }

    AnimatedContent(
        targetState = selectedAlertId,
        transitionSpec = { fadeIn(tween(220)) togetherWith fadeOut(tween(160)) },
        label = "alertScreenMode"
    ) { alertId ->
        if (alertId == null) {
            AlertListMode(
                alerts = alerts,
                onSelect = { selectedAlertId = it.id }
            )
        } else {
            val alert = alerts.firstOrNull { it.id == alertId }
            if (alert != null) {
                AlertDetailMode(
                    alert = alert,
                    onBack = { selectedAlertId = null }
                )
            }
        }
    }
}

@Composable
private fun AlertListMode(
    alerts: List<SosIncident>,
    onSelect: (SosIncident) -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
            Text(
                text = "Nearby SOS Alerts",
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onSurface,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "Open an alert to view the location and respond quickly.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        alerts.forEach { alert ->
            AlertListCard(
                alert = alert,
                onClick = withHaptic(AppHapticEvent.TAP) { onSelect(alert) }
            )
        }
    }
}

@Composable
private fun AlertListCard(
    alert: SosIncident,
    onClick: () -> Unit
) {
    val accentColor = alertAccent(alert.severity)

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .hapticClickable(hapticEvent = AppHapticEvent.TAP, onClick = onClick),
        shape = MaterialTheme.shapes.large,
        color = MaterialTheme.colorScheme.surface,
        border = BorderStroke(1.dp, accentColor.copy(alpha = 0.28f))
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text(
                        text = "Someone nearby needs your help",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSurface,
                        fontWeight = FontWeight.SemiBold
                    )
                    Text(
                        text = alert.address,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
                AlertStatePill(
                    text = "Live",
                    tint = accentColor
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                AlertInfoPill(
                    icon = { Icon(Icons.Filled.MyLocation, null, modifier = Modifier.size(14.dp)) },
                    label = distanceLabel(alert.distanceMeters),
                    tint = Sky500,
                    modifier = Modifier.weight(1f)
                )
                AlertInfoPill(
                    icon = { Icon(Icons.Filled.AccessTime, null, modifier = Modifier.size(14.dp)) },
                    label = minutesAgoLabel(alert.duration),
                    tint = Amber500,
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

@Composable
private fun AlertDetailMode(
    alert: SosIncident,
    onBack: () -> Unit
) {
    var pendingAction by remember { mutableStateOf<AlertResponseAction?>(null) }
    val visibleResponders = remember(alert.id) {
        alert.nearbyResponders.filter { it.state != ResponderState.NONE }
    }
    val fastestEtaMinutes = remember(alert.id) {
        visibleResponders.minOfOrNull { it.etaMinutes }?.coerceAtLeast(1) ?: 1
    }
    val mapStats = remember(alert.id) {
        listOf(
            MapFooterStat(distanceValue(alert.distanceMeters), "Distance", Coral500),
            MapFooterStat(alert.responderCount.toString(), "Helpers", Mint500),
            MapFooterStat(minutesValue(fastestEtaMinutes), "Away", Sky500),
            MapFooterStat(alert.arrivedCount.toString(), "Arrived", Amber500)
        )
    }

    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.large,
        color = MaterialTheme.colorScheme.surface,
        border = BorderStroke(1.dp, alertAccent(alert.severity).copy(alpha = 0.34f))
    ) {
        Column(
            modifier = Modifier.padding(18.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    FilledTonalIconButton(
                        onClick = withHaptic(AppHapticEvent.TAP, onBack),
                        colors = IconButtonDefaults.filledTonalIconButtonColors(
                            containerColor = Coral500.copy(alpha = 0.14f),
                            contentColor = MaterialTheme.colorScheme.onSurface
                        )
                    ) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back to alert list")
                    }
                }
            }

            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text(
                    text = "Someone needs your help",
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onSurface,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = minutesAgoLabel(alert.duration),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = alert.address,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                AlertInfoPill(
                    icon = { Icon(Icons.Filled.LocationOn, null, modifier = Modifier.size(14.dp)) },
                    label = distanceLabel(alert.distanceMeters),
                    tint = Coral500,
                    modifier = Modifier.weight(1f)
                )
                AlertInfoPill(
                    icon = { Icon(Icons.Filled.AccessTime, null, modifier = Modifier.size(14.dp)) },
                    label = minutesAgoLabel(alert.duration),
                    tint = Mint500,
                    modifier = Modifier.weight(1f)
                )
            }

            MapPlaceholderCard(
                title = "Victim's location",
                subtitle = alert.address,
                showRoute = true,
                footerText = null,
                fullscreenStats = mapStats,
                fullscreenLocationLabel = alert.address
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                HeroMetric(
                    value = minutesAgoMetric(alert.duration),
                    label = "Broadcasted",
                    modifier = Modifier.weight(1f),
                    accentColor = Coral500
                )
                HeroMetric(
                    value = minutesValue(fastestEtaMinutes),
                    label = "Away",
                    modifier = Modifier.weight(1f),
                    accentColor = Mint500
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Button(
                    onClick = withHaptic(AppHapticEvent.TAP) { pendingAction = AlertResponseAction.COMING },
                    modifier = Modifier
                        .weight(1f)
                        .height(52.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Coral500,
                        contentColor = MaterialTheme.colorScheme.onPrimary
                    )
                ) {
                    Icon(Icons.AutoMirrored.Filled.DirectionsWalk, null, modifier = Modifier.size(18.dp))
                    Spacer(Modifier.width(6.dp))
                    Text("I am coming", fontWeight = FontWeight.Bold)
                }
                Button(
                    onClick = withHaptic(AppHapticEvent.TAP) { pendingAction = AlertResponseAction.ARRIVED },
                    modifier = Modifier
                        .weight(1f)
                        .height(52.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Mint500,
                        contentColor = MaterialTheme.colorScheme.onPrimary
                    )
                ) {
                    Icon(Icons.Filled.Person, null, modifier = Modifier.size(18.dp))
                    Spacer(Modifier.width(6.dp))
                    Text("Mark arrived", fontWeight = FontWeight.Bold)
                }
            }

            if (alert.timeline.isNotEmpty()) {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(
                        text = "Live timeline",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSurface,
                        fontWeight = FontWeight.SemiBold
                    )
                    alert.timeline.forEachIndexed { index, event ->
                        TimelineCard(
                            time = event.time,
                            title = event.label,
                            detail = event.detail,
                            animIndex = index
                        )
                    }
                }
            }

            if (visibleResponders.isNotEmpty()) {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(
                        text = "Nearby helpers",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSurface,
                        fontWeight = FontWeight.SemiBold
                    )
                    visibleResponders.forEachIndexed { index, responder ->
                        ResponderCard(
                            responder = responder,
                            animIndex = index
                        )
                    }
                }
            }
        }
    }

    pendingAction?.let { action ->
        AlertDialog(
            onDismissRequest = { pendingAction = null },
            title = {
                Text(
                    text = if (action == AlertResponseAction.COMING) "Confirm response" else "Confirm arrival"
                )
            },
            text = {
                Text(
                    text = if (action == AlertResponseAction.COMING) {
                        "Confirm that you are heading to this location."
                    } else {
                        "Confirm that you have reached the victim's location."
                    }
                )
            },
            confirmButton = {
                TextButton(onClick = withHaptic(AppHapticEvent.CONFIRM) { pendingAction = null }) {
                    Text(if (action == AlertResponseAction.COMING) "I am coming" else "Mark arrived")
                }
            },
            dismissButton = {
                TextButton(onClick = withHaptic(AppHapticEvent.REJECT) { pendingAction = null }) {
                    Text("Cancel")
                }
            }
        )
    }
}

@Composable
private fun AlertInfoPill(
    icon: @Composable () -> Unit,
    label: String,
    tint: Color,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier,
        shape = MaterialTheme.shapes.medium,
        color = tint.copy(alpha = 0.10f),
        border = BorderStroke(1.dp, tint.copy(alpha = 0.24f))
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            icon()
            Text(
                text = label,
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurface,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Composable
private fun AlertStatePill(text: String, tint: Color) {
    Surface(
        shape = MaterialTheme.shapes.medium,
        color = tint.copy(alpha = 0.12f),
        border = BorderStroke(1.dp, tint.copy(alpha = 0.28f))
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.labelMedium,
            color = tint,
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
        )
    }
}

private fun alertAccent(severity: IncidentSeverity): Color =
    when (severity) {
        IncidentSeverity.HIGH -> Coral500
        IncidentSeverity.MEDIUM -> Amber500
        IncidentSeverity.LOW -> Mint500
    }

private fun distanceValue(distanceMeters: Int): String =
    if (distanceMeters < 1000) {
        "${distanceMeters}m"
    } else {
        String.format(Locale.US, "%.1fkm", distanceMeters / 1000f)
    }

private fun distanceLabel(distanceMeters: Int): String = distanceValue(distanceMeters)

private fun minutesAgoCount(duration: String): Int {
    val match = Regex("""(\d+):(\d{2})""").find(duration) ?: return 1
    return match.groupValues[1].toIntOrNull()?.coerceAtLeast(1) ?: 1
}

private fun minutesAgoLabel(duration: String): String {
    val minutes = minutesAgoCount(duration)
    return if (minutes == 1) "1 min ago" else "$minutes min ago"
}

private fun minutesAgoMetric(duration: String): String {
    val minutes = minutesAgoCount(duration)
    return if (minutes == 1) "1 min" else "$minutes min"
}

private fun minutesValue(minutes: Int): String =
    if (minutes == 1) "1 min" else "$minutes min"

private enum class AlertResponseAction {
    COMING,
    ARRIVED
}
