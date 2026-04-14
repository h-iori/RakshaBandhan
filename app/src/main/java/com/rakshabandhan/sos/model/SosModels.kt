package com.rakshabandhan.sos.model

import androidx.compose.runtime.Immutable

// ── Enums ────────────────────────────────────────────────────────────────────
enum class AppScreen { AUTH, HOME, ACTIVE_SOS, RESPONDER_ALERT, HISTORY }

enum class SosState   { ACTIVE, ENDING, STOPPED }
enum class ResponderState { NONE, COMING, ARRIVED }
enum class IncidentSeverity { LOW, MEDIUM, HIGH }

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
data class SosIncident(
    val id: String,
    val title: String,
    val address: String,
    val startedAt: String,
    val duration: String,
    val responderCount: Int,
    val arrivedCount: Int,
    val state: SosState,
    val note: String,
    val severity: IncidentSeverity = IncidentSeverity.MEDIUM
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

// ── Dummy data ────────────────────────────────────────────────────────────────
val demoUser = UserProfile(
    name             = "Priya Sharma",
    phone            = "+91 98765 43210",
    avatarInitials   = "PS",
    trustedContacts  = 5,
    safeZonesCount   = 3,
    totalSosSent     = 2
)

val demoIncidents = listOf(
    SosIncident(
        id             = "SOS-2048",
        title          = "Active escort request",
        address        = "MG Road, Bengaluru",
        startedAt      = "Tonight, 10:14 PM",
        duration       = "08:42",
        responderCount = 17,
        arrivedCount   = 4,
        state          = SosState.ACTIVE,
        note           = "Crowded zone, 500m radius broadcast enabled",
        severity       = IncidentSeverity.HIGH
    ),
    SosIncident(
        id             = "SOS-2031",
        title          = "Late-night commute",
        address        = "Koramangala, Bengaluru",
        startedAt      = "Yesterday, 11:30 PM",
        duration       = "05:20",
        responderCount = 9,
        arrivedCount   = 2,
        state          = SosState.STOPPED,
        note           = "Resolved after helper escort",
        severity       = IncidentSeverity.MEDIUM
    ),
    SosIncident(
        id             = "SOS-2019",
        title          = "Resolved night commute",
        address        = "Bandra West, Mumbai",
        startedAt      = "2 days ago, 09:08 PM",
        duration       = "14:59",
        responderCount = 11,
        arrivedCount   = 2,
        state          = SosState.STOPPED,
        note           = "Auto-stopped after timeout",
        severity       = IncidentSeverity.LOW
    ),
    SosIncident(
        id             = "SOS-2013",
        title          = "Campus alert",
        address        = "Gachibowli, Hyderabad",
        startedAt      = "3 days ago, 11:46 PM",
        duration       = "06:15",
        responderCount = 8,
        arrivedCount   = 3,
        state          = SosState.STOPPED,
        note           = "Quick response from nearby verified users",
        severity       = IncidentSeverity.MEDIUM
    )
)

val demoResponders = listOf(
    ResponderItem("Aarav Singh",   220, 2, ResponderState.COMING,  "AS", 4.9f, 34),
    ResponderItem("Meera Pillai",  310, 3, ResponderState.COMING,  "MP", 4.7f, 18),
    ResponderItem("Nikhil Joshi",  430, 4, ResponderState.ARRIVED, "NJ", 5.0f, 41),
    ResponderItem("Preethi Nair",  480, 5, ResponderState.NONE,    "PN", 4.6f,  9),
    ResponderItem("Rohan Mehta",   650, 7, ResponderState.COMING,  "RM", 4.8f, 22)
)

val demoTimeline = listOf(
    TimelineEvent("10:14 PM", "SOS confirmed",            "Nearby users within 500m notified immediately",   true),
    TimelineEvent("10:15 PM", "Live location shared",      "Location updates streaming in real time",         true),
    TimelineEvent("10:16 PM", "Responder marked coming",   "Aarav Singh selected 'I am coming'",              true),
    TimelineEvent("10:18 PM", "Second responder en route", "Meera Pillai started navigation to the scene",    true),
    TimelineEvent("10:20 PM", "First responder arrived",   "Nikhil Joshi reached the scene and confirmed",    true),
    TimelineEvent("10:22 PM", "SOS ending…",               "Victim confirmed safe — 30-second cooldown",      false)
)

val demoQuickStats = listOf(
    QuickStat("500m",    "Alert radius",      "radar"),
    QuickStat("15 min",  "Auto-stop",         "timer"),
    QuickStat("17",      "Helpers nearby",    "group"),
    QuickStat("2–4 min", "Avg response",      "speed")
)
