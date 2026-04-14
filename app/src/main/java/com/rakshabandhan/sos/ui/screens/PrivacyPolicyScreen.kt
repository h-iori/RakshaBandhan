package com.rakshabandhan.sos.ui.screens

import androidx.compose.animation.core.tween
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.BorderStroke
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
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Policy
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.rakshabandhan.sos.ui.components.DemoFrame
import com.rakshabandhan.sos.ui.theme.Coral500
import com.rakshabandhan.sos.ui.theme.Mint500
import com.rakshabandhan.sos.ui.theme.Navy800
import com.rakshabandhan.sos.ui.theme.Sky500
import com.rakshabandhan.sos.ui.theme.Slate100
import com.rakshabandhan.sos.ui.theme.Slate200
import com.rakshabandhan.sos.ui.theme.Slate700
import kotlinx.coroutines.delay

private data class PolicySection(val title: String, val body: String)

private val privacySections = listOf(
    PolicySection(
        "1. Information We Collect",
        "We collect information you provide directly to us when you create an account, " +
                "including your name, email address, phone number, and gender. We also collect " +
                "your location data during an active SOS event to broadcast your position to " +
                "nearby verified responders within the configured alert radius."
    ),
    PolicySection(
        "2. How We Use Your Information",
        "Your information is used solely to operate the RakshaBandhan SOS platform. " +
                "Specifically: to authenticate your identity, to broadcast your location during " +
                "SOS events, to connect you with nearby responders, and to maintain your incident " +
                "history for your own review. We do not sell, rent, or share your personal data " +
                "with any third-party advertisers."
    ),
    PolicySection(
        "3. Location Data",
        "Location data is only collected and transmitted during an active SOS session. " +
                "Once an SOS event is stopped or times out, location broadcasting ceases immediately. " +
                "Historical location data associated with past SOS incidents is stored securely and " +
                "accessible only by you through your incident history."
    ),
    PolicySection(
        "4. Data Security",
        "We implement industry-standard security measures including end-to-end encryption " +
                "for location broadcasts and secure HTTPS connections for all API communications. " +
                "Your data is stored on encrypted servers and access is restricted to authorized " +
                "personnel only. In the event of a data breach, we will notify you within 72 hours."
    ),
    PolicySection(
        "5. Data Retention",
        "We retain your account information for as long as your account is active. " +
                "You may request deletion of your account and all associated data at any time " +
                "by contacting us at privacy@rakshabandhan.app. Deleted data is permanently " +
                "removed from our systems within 30 days of the request."
    ),
    PolicySection(
        "6. Third-Party Services",
        "RakshaBandhan SOS does not integrate third-party advertising services. " +
                "We may use analytics tools to improve app performance. Any third-party service " +
                "providers are contractually bound to protect your data and may not use it for " +
                "their own purposes."
    ),
    PolicySection(
        "7. Children's Privacy",
        "Our service is not directed to individuals under the age of 13. We do not " +
                "knowingly collect personal information from children. If you believe a child " +
                "has provided us personal information, please contact us and we will promptly " +
                "delete such information."
    ),
    PolicySection(
        "8. Changes to This Policy",
        "We may update this Privacy Policy from time to time. We will notify you of " +
                "any significant changes by sending a notification to your registered email address " +
                "or through a prominent notice within the app. Continued use of the app after " +
                "changes constitutes acceptance of the updated policy."
    ),
    PolicySection(
        "9. Contact Us",
        "If you have questions or concerns about this Privacy Policy or our data practices, " +
                "please contact us at:\n\nEmail: privacy@rakshabandhan.app\n" +
                "Address: RakshaBandhan Technologies Pvt. Ltd., Bengaluru, Karnataka, India 560001"
    )
)

@Composable
fun PrivacyPolicyScreen(onBack: () -> Unit) {
    var visible by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) { delay(80); visible = true }

    val contentAlpha by animateFloatAsState(
        targetValue = if (visible) 1f else 0f,
        animationSpec = tween(500),
        label = "ppAlpha"
    )

    DemoFrame(
        title = "Privacy Policy",
        subtitle = "Last updated: January 2024",
        trailing = {
            IconButton(onClick = onBack) {
                Icon(Icons.Filled.ArrowBack, contentDescription = "Back", tint = Slate200)
            }
        }
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.alpha(contentAlpha)
        ) {
            // Header banner
            Surface(
                shape = MaterialTheme.shapes.medium,
                color = Sky500.copy(alpha = 0.10f),
                border = BorderStroke(1.dp, Sky500.copy(alpha = 0.3f))
            ) {
                Row(
                    modifier = Modifier.padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Icon(Icons.Filled.Policy, null, tint = Sky500, modifier = Modifier.size(22.dp))
                    Column {
                        Text(
                            "Your Privacy Matters",
                            style = MaterialTheme.typography.titleSmall,
                            color = Slate100,
                            fontWeight = FontWeight.SemiBold
                        )
                        Text(
                            "We are committed to protecting your personal data and your right to privacy.",
                            style = MaterialTheme.typography.bodySmall,
                            color = Slate200
                        )
                    }
                }
            }

            // Policy sections
            privacySections.forEachIndexed { index, section ->
                PolicyBlock(section = section, accent = sectionAccentColor(index))
            }

            Spacer(Modifier.height(8.dp))
        }
    }
}

@Composable
private fun PolicyBlock(section: PolicySection, accent: androidx.compose.ui.graphics.Color) {
    Surface(
        shape = MaterialTheme.shapes.medium,
        color = Navy800,
        border = BorderStroke(1.dp, accent.copy(alpha = 0.2f))
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                section.title,
                style = MaterialTheme.typography.titleSmall,
                color = accent,
                fontWeight = FontWeight.SemiBold
            )
            Text(
                section.body,
                style = MaterialTheme.typography.bodySmall,
                color = Slate200,
                lineHeight = MaterialTheme.typography.bodySmall.lineHeight
            )
        }
    }
}

private fun sectionAccentColor(index: Int): androidx.compose.ui.graphics.Color {
    return when (index % 4) {
        0 -> Coral500
        1 -> Sky500
        2 -> Mint500
        else -> androidx.compose.ui.graphics.Color(0xFFF3B23E) // Amber500
    }
}