package com.rakshabandhan.sos.model

import androidx.compose.runtime.Immutable

// ── Enums ────────────────────────────────────────────────────────────────────
enum class AppScreen { AUTH, HOME, ACTIVE_SOS, RESPONDER_ALERT, HISTORY }

enum class SosState { ACTIVE, ENDING, STOPPED }
enum class ResponderState { NONE, COMING, ARRIVED }
enum class IncidentSeverity { LOW, MEDIUM, HIGH }
enum class HistoryRole { HELPED_SOMEONE, ASKED_FOR_HELP }

// ── Data classes ─────────────────────────────────────────────────────────────
@Immutable
data class UserProfile(
    val name: String,
    val phone: String,
    val avatarInitials: String,
    val trustedContacts: Int,
    val safeZonesCount: Int,
    val totalSosSent: Int
)

@Immutable
data class ResponderItem(
    val name: String,
    val distanceMeters: Int,
    val etaMinutes: Int,
    val state: ResponderState,
    val avatar: String,
    val rating: Float = 4.8f,
    val responseCount: Int = 12
)

@Immutable
data class TimelineEvent(
    val time: String,
    val label: String,
    val detail: String,
    val isCompleted: Boolean = true
)

@Immutable
data class QuickStat(
    val value: String,
    val label: String,
    val iconName: String
)

@Immutable
data class SosIncident(
    val id: String,
    val title: String,
    val address: String,
    val startedAt: String,
    val endedAt: String,
    val duration: String,
    val distanceMeters: Int,
    val responderCount: Int,
    val arrivedCount: Int,
    val state: SosState,
    val note: String,
    val historyRole: HistoryRole = HistoryRole.ASKED_FOR_HELP,
    val severity: IncidentSeverity = IncidentSeverity.MEDIUM,
    val timeline: List<TimelineEvent> = emptyList(),
    val nearbyResponders: List<ResponderItem> = emptyList()
)

private fun timelineOf(vararg events: Triple<String, String, String>): List<TimelineEvent> =
    events.map { (time, label, detail) -> TimelineEvent(time = time, label = label, detail = detail) }

// ── Dummy data ────────────────────────────────────────────────────────────────
val demoUser = UserProfile(
    name = "Priya Sharma",
    phone = "+91 98765 43210",
    avatarInitials = "PS",
    trustedContacts = 5,
    safeZonesCount = 3,
    totalSosSent = 2
)

val demoResponders = listOf(
    ResponderItem("Aarav Singh", 220, 2, ResponderState.COMING, "AS", 4.9f, 34),
    ResponderItem("Meera Pillai", 310, 3, ResponderState.COMING, "MP", 4.7f, 18),
    ResponderItem("Nikhil Joshi", 430, 4, ResponderState.ARRIVED, "NJ", 5.0f, 41),
    ResponderItem("Preethi Nair", 480, 5, ResponderState.NONE, "PN", 4.6f, 9),
    ResponderItem("Rohan Mehta", 650, 7, ResponderState.COMING, "RM", 4.8f, 22)
)

val demoTimeline = timelineOf(
    Triple("10:14 PM", "SOS confirmed", "Nearby users within 500m notified immediately"),
    Triple("10:15 PM", "Live location shared", "Location updates streaming in real time"),
    Triple("10:16 PM", "Responder marked coming", "Aarav Singh selected 'I am coming'"),
    Triple("10:18 PM", "Second responder en route", "Meera Pillai started navigation to the scene"),
    Triple("10:20 PM", "First responder arrived", "Nikhil Joshi reached the scene and confirmed"),
    Triple("10:22 PM", "SOS ending", "Victim confirmed safe after manual acknowledgement")
)

val demoIncomingAlerts = listOf(
    SosIncident(
        id = "ALERT-7812",
        title = "Escort request from Aisha Khan",
        address = "MG Road Metro Exit 3, Bengaluru",
        startedAt = "Apr 25, 2026, 10:24 PM",
        endedAt = "Apr 25, 2026, 10:41 PM",
        duration = "02:08 open",
        distanceMeters = 430,
        responderCount = 6,
        arrivedCount = 1,
        state = SosState.ACTIVE,
        note = "Caller is waiting near the metro gate and has shared a live route home.",
        historyRole = HistoryRole.HELPED_SOMEONE,
        severity = IncidentSeverity.HIGH,
        timeline = timelineOf(
            Triple("Apr 25, 2026, 10:24 PM", "Broadcast started", "Alert reached verified helpers within a 500m radius."),
            Triple("Apr 25, 2026, 10:25 PM", "Route shared", "The walk-home route became available to responders."),
            Triple("Apr 25, 2026, 10:26 PM", "You accepted the request", "You started navigation toward the pickup point.")
        ),
        nearbyResponders = listOf(demoResponders[0], demoResponders[1], demoResponders[2])
    ),
    SosIncident(
        id = "ALERT-7804",
        title = "Night commute check-in",
        address = "Koramangala 5th Block, Bengaluru",
        startedAt = "Apr 24, 2026, 10:11 PM",
        endedAt = "Apr 24, 2026, 10:29 PM",
        duration = "05:41 open",
        distanceMeters = 760,
        responderCount = 4,
        arrivedCount = 0,
        state = SosState.ACTIVE,
        note = "The caller requested virtual escort support while walking between cabs.",
        historyRole = HistoryRole.HELPED_SOMEONE,
        severity = IncidentSeverity.MEDIUM,
        timeline = timelineOf(
            Triple("Apr 24, 2026, 10:11 PM", "Help request created", "A low-friction alert was sent to nearby volunteers."),
            Triple("Apr 24, 2026, 10:13 PM", "Route confirmed", "The destination was verified against the safe-zone map."),
            Triple("Apr 24, 2026, 10:15 PM", "You joined the response", "You monitored the route with nearby helpers.")
        ),
        nearbyResponders = listOf(demoResponders[1], demoResponders[4], demoResponders[3])
    ),
    SosIncident(
        id = "ALERT-7797",
        title = "Campus perimeter alert",
        address = "Gachibowli Stadium Road, Hyderabad",
        startedAt = "Apr 23, 2026, 9:58 PM",
        endedAt = "Apr 23, 2026, 10:18 PM",
        duration = "11:09 open",
        distanceMeters = 1240,
        responderCount = 8,
        arrivedCount = 2,
        state = SosState.ACTIVE,
        note = "A student flagged an unsafe tailing incident outside the south campus gate.",
        historyRole = HistoryRole.HELPED_SOMEONE,
        severity = IncidentSeverity.HIGH,
        timeline = timelineOf(
            Triple("Apr 23, 2026, 9:58 PM", "High-priority alert", "The system elevated the alert after the second distress tap."),
            Triple("Apr 23, 2026, 10:01 PM", "Campus security pinged", "The nearest support desk was informed automatically."),
            Triple("Apr 23, 2026, 10:06 PM", "You arrived nearby", "You and another responder reached the area within minutes.")
        ),
        nearbyResponders = listOf(demoResponders[2], demoResponders[0], demoResponders[4])
    ),
    SosIncident(
        id = "ALERT-7788",
        title = "Late shift ride pickup",
        address = "Bandra Kurla Complex Gate 2, Mumbai",
        startedAt = "Apr 22, 2026, 9:46 PM",
        endedAt = "Apr 22, 2026, 10:00 PM",
        duration = "14:34 open",
        distanceMeters = 1860,
        responderCount = 5,
        arrivedCount = 1,
        state = SosState.ENDING,
        note = "The user requested standby coverage until the cab plate was cross-checked.",
        historyRole = HistoryRole.HELPED_SOMEONE,
        severity = IncidentSeverity.LOW,
        timeline = timelineOf(
            Triple("Apr 22, 2026, 9:46 PM", "Ride verification started", "Nearby volunteers were asked to monitor the pickup."),
            Triple("Apr 22, 2026, 9:52 PM", "You validated the cab", "The ride details were cross-checked on-site."),
            Triple("Apr 22, 2026, 9:58 PM", "Request completed", "The rider confirmed the pickup was safe.")
        ),
        nearbyResponders = listOf(demoResponders[3], demoResponders[4], demoResponders[1])
    )
)

val demoIncidents = listOf(
    SosIncident(
        id = "SOS-2048",
        title = "Escort request completed",
        address = "MG Road, Bengaluru",
        startedAt = "Apr 25, 2026, 10:14 PM",
        endedAt = "Apr 25, 2026, 10:22 PM",
        duration = "08:42",
        distanceMeters = 410,
        responderCount = 17,
        arrivedCount = 4,
        state = SosState.ACTIVE,
        note = "Crowded zone, 500m radius broadcast enabled.",
        historyRole = HistoryRole.ASKED_FOR_HELP,
        severity = IncidentSeverity.HIGH,
        timeline = timelineOf(
            Triple("Apr 25, 2026, 10:14 PM", "Broadcast started", "Trusted responders within 500m received the alert."),
            Triple("Apr 25, 2026, 10:16 PM", "Escort matched", "The closest responder accepted and started navigation."),
            Triple("Apr 25, 2026, 10:21 PM", "Checkpoint reached", "You confirmed visual contact with responders.")
        ),
        nearbyResponders = listOf(demoResponders[0], demoResponders[1], demoResponders[2], demoResponders[4])
    ),
    SosIncident(
        id = "SOS-2031",
        title = "Late-night commute",
        address = "Koramangala, Bengaluru",
        startedAt = "Apr 24, 2026, 11:30 PM",
        endedAt = "Apr 24, 2026, 11:35 PM",
        duration = "05:20",
        distanceMeters = 620,
        responderCount = 9,
        arrivedCount = 2,
        state = SosState.STOPPED,
        note = "Resolved after helper escort reached the pickup lane.",
        historyRole = HistoryRole.ASKED_FOR_HELP,
        severity = IncidentSeverity.MEDIUM,
        timeline = timelineOf(
            Triple("Apr 24, 2026, 11:30 PM", "Broadcast started", "The SOS was pushed to the closest trusted volunteers."),
            Triple("Apr 24, 2026, 11:33 PM", "First helper en route", "A helper reached the road crossing in three minutes."),
            Triple("Apr 24, 2026, 11:35 PM", "Incident closed", "You dismissed the SOS after safe pickup.")
        ),
        nearbyResponders = listOf(demoResponders[1], demoResponders[2], demoResponders[3])
    ),
    SosIncident(
        id = "SOS-2019",
        title = "Resolved night commute",
        address = "Bandra West, Mumbai",
        startedAt = "Apr 23, 2026, 9:08 PM",
        endedAt = "Apr 23, 2026, 9:23 PM",
        duration = "14:59",
        distanceMeters = 1820,
        responderCount = 11,
        arrivedCount = 2,
        state = SosState.STOPPED,
        note = "Auto-stopped after timeout once the route stayed stable.",
        historyRole = HistoryRole.ASKED_FOR_HELP,
        severity = IncidentSeverity.LOW,
        timeline = timelineOf(
            Triple("Apr 23, 2026, 9:08 PM", "Broadcast started", "The route was shared with nearby community responders."),
            Triple("Apr 23, 2026, 9:15 PM", "Route stabilized", "Movement returned to the expected corridor and risk dropped."),
            Triple("Apr 23, 2026, 9:23 PM", "Auto-stop complete", "The timer completed and no further action was required.")
        ),
        nearbyResponders = listOf(demoResponders[4], demoResponders[0], demoResponders[3])
    ),
    SosIncident(
        id = "SOS-2013",
        title = "Campus alert",
        address = "Gachibowli, Hyderabad",
        startedAt = "Apr 22, 2026, 11:46 PM",
        endedAt = "Apr 22, 2026, 11:52 PM",
        duration = "06:15",
        distanceMeters = 980,
        responderCount = 8,
        arrivedCount = 3,
        state = SosState.STOPPED,
        note = "Quick response from nearby verified users closed the incident fast.",
        historyRole = HistoryRole.ASKED_FOR_HELP,
        severity = IncidentSeverity.MEDIUM,
        timeline = timelineOf(
            Triple("Apr 22, 2026, 11:46 PM", "Alert raised", "The caller triggered the alert from the campus walkway."),
            Triple("Apr 22, 2026, 11:49 PM", "Three responders committed", "Nearby helpers coordinated a safe escort cluster."),
            Triple("Apr 22, 2026, 11:52 PM", "Incident resolved", "You marked yourself safe after entering the hostel gate.")
        ),
        nearbyResponders = listOf(demoResponders[2], demoResponders[0], demoResponders[1])
    )
)

val demoHistoryIncidents = (demoIncidents + demoIncomingAlerts).sortedByDescending { it.startedAt }

val demoQuickStats = listOf(
    QuickStat("500m", "Alert radius", "radar"),
    QuickStat("15 min", "Auto-stop", "timer"),
    QuickStat("17", "Helpers nearby", "group"),
    QuickStat("2-4 min", "Avg response", "speed")
)
