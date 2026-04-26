package com.rakshabandhan.sos.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import com.rakshabandhan.sos.ui.haptics.AppHapticEvent
import com.rakshabandhan.sos.ui.haptics.withHaptic
import com.rakshabandhan.sos.ui.haptics.hapticClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.Event
import androidx.compose.material.icons.filled.Route
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.rakshabandhan.sos.model.SosIncident
import com.rakshabandhan.sos.ui.components.HeroMetric
import com.rakshabandhan.sos.ui.components.LinearMetricRow
import com.rakshabandhan.sos.ui.components.MapFooterStat
import com.rakshabandhan.sos.ui.components.MapPlaceholderCard
import com.rakshabandhan.sos.ui.components.ResponderCard
import com.rakshabandhan.sos.ui.components.TimelineCard
import com.rakshabandhan.sos.ui.theme.Amber500
import com.rakshabandhan.sos.ui.theme.CardSurface
import com.rakshabandhan.sos.ui.theme.Coral500
import com.rakshabandhan.sos.ui.theme.Mint500
import com.rakshabandhan.sos.ui.theme.Sky500
import com.rakshabandhan.sos.ui.theme.Slate100
import com.rakshabandhan.sos.ui.theme.Slate200
import com.rakshabandhan.sos.ui.theme.Slate700
import java.time.Duration
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import java.util.Locale

// ── Enums ──────────────────────────────────────────────────────────────────────

private enum class HistoryRecordFilter {
    HELPED_SOMEONE,
    ASKED_FOR_HELP
}

private enum class HistoryRecordKind {
    HELPED_SOMEONE,
    ASKED_FOR_HELP
}

// ── Date formatters ────────────────────────────────────────────────────────────

private val exactDateTimeFormatter: DateTimeFormatter =
    DateTimeFormatter.ofPattern("MMM d, yyyy 'at' h:mm a", Locale.US)

private val commaSeparatedDateTimeFormatter: DateTimeFormatter =
    DateTimeFormatter.ofPattern("MMM d, yyyy, h:mm a", Locale.US)

private val timeFormatter: DateTimeFormatter =
    DateTimeFormatter.ofPattern("h:mm a", Locale.US)

// ── Root screen ────────────────────────────────────────────────────────────────

@Composable
fun HistoryScreen(incidents: List<SosIncident>) {
    var selectedIncidentId by rememberSaveable { mutableStateOf<String?>(null) }
    var activeFilterName by rememberSaveable { mutableStateOf(HistoryRecordFilter.ASKED_FOR_HELP.name) }
    val activeFilter = parseHistoryRecordFilter(activeFilterName)

    BackHandler(enabled = selectedIncidentId != null) {
        selectedIncidentId = null
    }

    // Directional slide: forward (list → detail) slides right in,
    // backward (detail → list) slides left in — gives spatial awareness.
    AnimatedContent(
        targetState = selectedIncidentId,
        transitionSpec = {
            if (targetState != null) {
                // Navigating into detail
                (slideInHorizontally(animationSpec = tween(300)) { it } +
                        fadeIn(tween(300))) togetherWith
                        (slideOutHorizontally(animationSpec = tween(300)) { -it / 4 } +
                                fadeOut(tween(200)))
            } else {
                // Navigating back to list
                (slideInHorizontally(animationSpec = tween(300)) { -it / 4 } +
                        fadeIn(tween(300))) togetherWith
                        (slideOutHorizontally(animationSpec = tween(300)) { it } +
                                fadeOut(tween(200)))
            }
        },
        label = "historyScreenMode"
    ) { incidentId ->
        if (incidentId == null) {
            HistoryListMode(
                incidents = incidents,
                activeFilter = activeFilter,
                onFilterSelected = { activeFilterName = it.name },
                onSelect = { selectedIncidentId = it.id }
            )
        } else {
            val incident = incidents.firstOrNull { it.id == incidentId }
            if (incident != null) {
                HistoryDetailMode(
                    incident = incident,
                    onBack = { selectedIncidentId = null }
                )
            }
        }
    }
}

// ── List mode ──────────────────────────────────────────────────────────────────

@Composable
private fun HistoryListMode(
    incidents: List<SosIncident>,
    activeFilter: HistoryRecordFilter,
    onFilterSelected: (HistoryRecordFilter) -> Unit,
    onSelect: (SosIncident) -> Unit
) {
    val filteredIncidents = incidents.filter { matchesFilter(it, activeFilter) }

    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {

        // Header row: title + animated record count side by side
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Bottom
        ) {
            Text(
                text = "History",
                style = MaterialTheme.typography.headlineSmall,
                color = Slate100,
                fontWeight = FontWeight.Bold
            )
            // Count animates when filter changes so the user sees the state update
            AnimatedContent(
                targetState = filteredIncidents.size,
                transitionSpec = { fadeIn(tween(200)) togetherWith fadeOut(tween(200)) },
                label = "recordCount"
            ) { count ->
                Text(
                    text = "$count records",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Slate200
                )
            }
        }

        // Compact pill-style filter chips — no label above, intrinsic width
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            HistoryFilterChip(
                label = "Helped someone",
                selected = activeFilter == HistoryRecordFilter.HELPED_SOMEONE,
                onClick = withHaptic(AppHapticEvent.SELECTION) { onFilterSelected(HistoryRecordFilter.HELPED_SOMEONE) }
            )
            HistoryFilterChip(
                label = "Asked for help",
                selected = activeFilter == HistoryRecordFilter.ASKED_FOR_HELP,
                onClick = withHaptic(AppHapticEvent.SELECTION) { onFilterSelected(HistoryRecordFilter.ASKED_FOR_HELP) }
            )
        }

        filteredIncidents.forEach { incident ->
            HistoryListCard(
                incident = incident,
                onClick = withHaptic(AppHapticEvent.TAP) { onSelect(incident) }
            )
        }
    }
}

// ── Compact pill chip ──────────────────────────────────────────────────────────

@Composable
private fun HistoryFilterChip(
    label: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    Surface(
        modifier = Modifier.hapticClickable(hapticEvent = AppHapticEvent.SELECTION, onClick = onClick),
        shape = RoundedCornerShape(999.dp),
        color = if (selected) Coral500.copy(alpha = 0.16f) else CardSurface,
        border = BorderStroke(
            1.dp,
            if (selected) Coral500.copy(alpha = 0.50f) else Slate700.copy(alpha = 0.40f)
        )
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelMedium,
            color = if (selected) Slate100 else Slate200,
            modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp),
            maxLines = 1
        )
    }
}

// ── List card ──────────────────────────────────────────────────────────────────

@Composable
private fun HistoryListCard(
    incident: SosIncident,
    onClick: () -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .hapticClickable(hapticEvent = AppHapticEvent.TAP, onClick = onClick),
        shape = MaterialTheme.shapes.large,
        color = CardSurface,
        // Single consistent border color — no severity-driven accent
        border = BorderStroke(1.dp, Sky500.copy(alpha = 0.22f))
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text(
                    text = historyListTitle(incident),
                    style = MaterialTheme.typography.titleMedium,
                    color = Slate100,
                    fontWeight = FontWeight.SemiBold
                )
                // Two lines so most addresses are fully visible without being cut off
                Text(
                    text = incident.address,
                    style = MaterialTheme.typography.bodySmall,
                    color = Slate200,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Relative date on list cards ("Today, 3:00 PM", "Yesterday, 11:00 AM", "3d ago")
                HistoryInfoPill(
                    icon = { Icon(Icons.Filled.AccessTime, null, modifier = Modifier.size(14.dp)) },
                    label = relativeDateTime(incident.startedAt),
                    tint = Amber500,
                    modifier = Modifier.weight(1f)
                )
                HistoryInfoPill(
                    icon = { Icon(Icons.Filled.Route, null, modifier = Modifier.size(14.dp)) },
                    label = distanceLabel(incident.distanceMeters),
                    tint = Sky500,
                    modifier = Modifier.weight(1f)
                )
            }
            // Note intentionally omitted — not displayed on list cards
        }
    }
}

// ── Detail mode ────────────────────────────────────────────────────────────────

@Composable
private fun HistoryDetailMode(
    incident: SosIncident,
    onBack: () -> Unit
) {
    // Exact date/time used throughout the detail view
    val startedAt = exactDateTime(incident.startedAt)
    val endedAt = endDateTime(incident)

    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.large,
        color = CardSurface,
        border = BorderStroke(1.dp, Sky500.copy(alpha = 0.28f))
    ) {
        Column(
            modifier = Modifier.padding(18.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            // ── Back header ──────────────────────────────────────────────────
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
                            containerColor = Sky500.copy(alpha = 0.14f),
                            contentColor = Slate100
                        )
                    ) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back to history list"
                        )
                    }
                    Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                        Text(
                            text = detailHeaderTitle(incident),
                            style = MaterialTheme.typography.titleMedium,
                            color = Slate100,
                            fontWeight = FontWeight.SemiBold
                        )
                        Text(
                            text = "Past incident record",
                            style = MaterialTheme.typography.bodySmall,
                            color = Slate200
                        )
                    }
                }
            }

            // ── Title + full address ─────────────────────────────────────────
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text(
                    text = historyListTitle(incident),
                    style = MaterialTheme.typography.titleLarge,
                    color = Slate100,
                    fontWeight = FontWeight.Bold
                )
                // Full address unrestricted in detail view
                Text(
                    text = incident.address,
                    style = MaterialTheme.typography.bodyMedium,
                    color = Slate200
                )
            }

            // ── Exact time pills (detail view uses full date/time) ───────────
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                HistoryInfoPill(
                    icon = { Icon(Icons.Filled.Event, null, modifier = Modifier.size(14.dp)) },
                    label = "Started: $startedAt",
                    tint = Amber500,
                    modifier = Modifier.weight(1f)
                )
                HistoryInfoPill(
                    icon = { Icon(Icons.Filled.AccessTime, null, modifier = Modifier.size(14.dp)) },
                    label = "Ended: $endedAt",
                    tint = Sky500,
                    modifier = Modifier.weight(1f)
                )
            }

            // Note intentionally omitted — not displayed in detail view

            // ── Map ──────────────────────────────────────────────────────────
            MapPlaceholderCard(
                title = mapRecordTitle(incident),
                subtitle = "Past record",
                showRoute = incident.arrivedCount > 0,
                fullscreenStatusLabel = "Past record",
                fullscreenStats = listOf(
                    MapFooterStat(incident.responderCount.toString(), "Responders", Sky500),
                    MapFooterStat(incident.arrivedCount.toString(), "Arrived", Mint500),
                    MapFooterStat(distanceValue(incident.distanceMeters), "Distance", Amber500)
                ),
                fullscreenLocationLabel = incident.address
            )

            // ── Hero metrics ─────────────────────────────────────────────────
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                HeroMetric(
                    value = incident.responderCount.toString(),
                    label = "Responders engaged",
                    modifier = Modifier.weight(1f),
                    accentColor = Sky500
                )
                HeroMetric(
                    value = incident.arrivedCount.toString(),
                    label = "Arrivals logged",
                    modifier = Modifier.weight(1f),
                    accentColor = Mint500
                )
            }

            // ── Linear metric rows ───────────────────────────────────────────
            LinearMetricRow(title = "Start time", value = startedAt)
            LinearMetricRow(title = "End time", value = endedAt)
            LinearMetricRow(title = "SOS active duration", value = normalizedDuration(incident.duration))
            LinearMetricRow(title = "Recorded distance", value = distanceLabel(incident.distanceMeters))

            // ── Incident timeline ────────────────────────────────────────────
            if (incident.timeline.isNotEmpty()) {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(
                        text = "Incident timeline",
                        style = MaterialTheme.typography.titleMedium,
                        color = Slate100,
                        fontWeight = FontWeight.SemiBold
                    )
                    incident.timeline.forEachIndexed { index, event ->
                        TimelineCard(
                            time = timelineDateTime(incident, event.time),
                            title = event.label,
                            detail = event.detail,
                            animIndex = index
                        )
                    }
                }
            }

            // ── Responder roster — always shown, with empty state ────────────
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(
                    text = "Responder roster",
                    style = MaterialTheme.typography.titleMedium,
                    color = Slate100,
                    fontWeight = FontWeight.SemiBold
                )
                if (incident.nearbyResponders.isEmpty()) {
                    Surface(
                        modifier = Modifier.fillMaxWidth(),
                        shape = MaterialTheme.shapes.medium,
                        color = CardSurface,
                        border = BorderStroke(1.dp, Slate700.copy(alpha = 0.30f))
                    ) {
                        Text(
                            text = "No responders were logged for this incident.",
                            style = MaterialTheme.typography.bodyMedium,
                            color = Slate200,
                            modifier = Modifier.padding(16.dp)
                        )
                    }
                } else {
                    incident.nearbyResponders.forEachIndexed { index, responder ->
                        ResponderCard(responder = responder, animIndex = index)
                    }
                }
            }
        }
    }
}

// ── Info pill ──────────────────────────────────────────────────────────────────

@Composable
private fun HistoryInfoPill(
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
                color = Slate100,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

// ── Pure helpers ───────────────────────────────────────────────────────────────

private fun matchesFilter(incident: SosIncident, filter: HistoryRecordFilter): Boolean =
    when (filter) {
        HistoryRecordFilter.HELPED_SOMEONE -> recordKind(incident) == HistoryRecordKind.HELPED_SOMEONE
        HistoryRecordFilter.ASKED_FOR_HELP -> recordKind(incident) == HistoryRecordKind.ASKED_FOR_HELP
    }

private fun recordKind(incident: SosIncident): HistoryRecordKind {
    // note field is used internally for classification only — never displayed in UI
    val content = buildString {
        append(incident.title.lowercase(Locale.US))
        append(' ')
        append(incident.note.lowercase(Locale.US))
        incident.timeline.forEach { event ->
            append(' ')
            append(event.label.lowercase(Locale.US))
            append(' ')
            append(event.detail.lowercase(Locale.US))
        }
    }

    val helpedSignals = listOf(
        "request from",
        "caller is",
        "caller requested",
        "helper accepted",
        "pickup point",
        "night commute check-in"
    )

    return if (helpedSignals.any { content.contains(it) }) {
        HistoryRecordKind.HELPED_SOMEONE
    } else {
        HistoryRecordKind.ASKED_FOR_HELP
    }
}

private fun historyListTitle(incident: SosIncident): String =
    when (recordKind(incident)) {
        HistoryRecordKind.HELPED_SOMEONE -> {
            val person = incident.title
                .substringAfter("from ", "")
                .takeIf { it.isNotBlank() && it != incident.title }
            if (person != null) "You helped $person" else "You helped with ${incident.title}"
        }
        HistoryRecordKind.ASKED_FOR_HELP -> "You asked for help: ${pastTitle(incident.title)}"
    }

private fun detailHeaderTitle(incident: SosIncident): String =
    when (recordKind(incident)) {
        HistoryRecordKind.HELPED_SOMEONE -> "Help you provided"
        HistoryRecordKind.ASKED_FOR_HELP -> "Your SOS history"
    }

private fun mapRecordTitle(incident: SosIncident): String =
    when (recordKind(incident)) {
        HistoryRecordKind.HELPED_SOMEONE -> "Past help record"
        HistoryRecordKind.ASKED_FOR_HELP -> pastTitle(incident.title)
    }

private fun pastTitle(title: String): String =
    when (title.lowercase(Locale.US)) {
        "active escort request" -> "Escort request completed"
        else -> title
    }

/**
 * Relative date for list cards — keeps cards scannable.
 * Examples: "Today, 3:00 PM" / "Yesterday, 11:30 AM" / "3d ago, 9:00 AM" / "Mar 12, 9:00 AM"
 */
private fun relativeDateTime(rawValue: String): String {
    val dt = parseIncidentDateTime(rawValue) ?: return rawValue
    val today = LocalDate.now()
    val date = dt.toLocalDate()
    val time = dt.format(timeFormatter)
    return when {
        date == today -> "Today, $time"
        date == today.minusDays(1) -> "Yesterday, $time"
        else -> {
            val days = ChronoUnit.DAYS.between(date, today)
            if (days in 2..6) "${days}d ago, $time"
            else dt.format(DateTimeFormatter.ofPattern("MMM d", Locale.US)) + ", $time"
        }
    }
}

/** Full exact date/time for detail view. */
private fun exactDateTime(rawValue: String): String =
    parseIncidentDateTime(rawValue)?.format(exactDateTimeFormatter) ?: rawValue

private fun timelineDateTime(incident: SosIncident, eventTime: String): String {
    val incidentStart = parseIncidentDateTime(incident.startedAt) ?: return eventTime
    parseTimeOnly(eventTime)?.let { time ->
        var candidate = LocalDateTime.of(incidentStart.toLocalDate(), time)
        if (candidate.isBefore(incidentStart.minusHours(6))) {
            candidate = candidate.plusDays(1)
        }
        return candidate.format(exactDateTimeFormatter)
    }
    return parseIncidentDateTime(eventTime)?.format(exactDateTimeFormatter) ?: eventTime
}

private fun endDateTime(incident: SosIncident): String {
    val start = parseIncidentDateTime(incident.startedAt) ?: return incident.startedAt
    val duration = parseDuration(incident.duration) ?: return incident.startedAt
    return start.plus(duration).format(exactDateTimeFormatter)
}

private fun normalizedDuration(rawValue: String): String {
    val duration = parseDuration(rawValue) ?: return rawValue
    val hours = duration.toHours()
    val minutes = duration.minusHours(hours).toMinutes()
    val seconds = duration.minusHours(hours).minusMinutes(minutes).seconds
    return buildString {
        if (hours > 0) append("${hours}h ")
        if (minutes > 0 || hours > 0) append("${minutes}m ")
        append("${seconds}s")
    }.trim()
}

private fun parseIncidentDateTime(rawValue: String): LocalDateTime? {
    val trimmed = rawValue.trim()
    val today = LocalDate.now()

    return when {
        trimmed.startsWith("Tonight,", ignoreCase = true) ->
            parseTimeOnly(trimmed.substringAfter(',').trim())?.atDate(today)

        trimmed.startsWith("Yesterday,", ignoreCase = true) ->
            parseTimeOnly(trimmed.substringAfter(',').trim())?.atDate(today.minusDays(1))

        Regex("""^(\d+)\s+days ago,\s+(.+)$""", RegexOption.IGNORE_CASE)
            .matchEntire(trimmed) != null -> {
            val match = Regex("""^(\d+)\s+days ago,\s+(.+)$""", RegexOption.IGNORE_CASE)
                .matchEntire(trimmed)
            val days = match?.groupValues?.get(1)?.toLongOrNull() ?: return null
            val time = match.groupValues[2]
            parseTimeOnly(time)?.atDate(today.minusDays(days))
        }

        trimmed.contains(" at ", ignoreCase = true) || trimmed.count { it == ',' } >= 2 ->
            parseExactDateTime(trimmed)

        else -> parseTimeOnly(trimmed)?.atDate(today)
    }
}

private fun parseHistoryRecordFilter(rawValue: String): HistoryRecordFilter =
    enumValues<HistoryRecordFilter>().firstOrNull { it.name == rawValue }
        ?: HistoryRecordFilter.ASKED_FOR_HELP

private fun parseExactDateTime(rawValue: String): LocalDateTime? =
    runCatching { LocalDateTime.parse(rawValue, exactDateTimeFormatter) }.getOrNull()
        ?: runCatching { LocalDateTime.parse(rawValue, commaSeparatedDateTimeFormatter) }.getOrNull()

private fun parseTimeOnly(rawValue: String): LocalTime? =
    runCatching { LocalTime.parse(rawValue.trim(), timeFormatter) }.getOrNull()

private fun parseDuration(rawValue: String): Duration? {
    val match = Regex("""(\d{1,2}):(\d{2})""").find(rawValue) ?: return null
    val minutes = match.groupValues[1].toLongOrNull() ?: return null
    val seconds = match.groupValues[2].toLongOrNull() ?: return null
    return Duration.ofMinutes(minutes).plusSeconds(seconds)
}

private fun distanceValue(distanceMeters: Int): String =
    if (distanceMeters < 1000) "${distanceMeters}m"
    else String.format(Locale.US, "%.1fkm", distanceMeters / 1000f)

private fun distanceLabel(distanceMeters: Int): String = "${distanceValue(distanceMeters)} away"