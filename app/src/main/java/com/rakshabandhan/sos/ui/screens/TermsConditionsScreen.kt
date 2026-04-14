package com.rakshabandhan.sos.ui.screens

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Gavel
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
import com.rakshabandhan.sos.ui.theme.Amber500
import com.rakshabandhan.sos.ui.theme.Coral500
import com.rakshabandhan.sos.ui.theme.Mint500
import com.rakshabandhan.sos.ui.theme.Navy800
import com.rakshabandhan.sos.ui.theme.Sky500
import com.rakshabandhan.sos.ui.theme.Slate100
import com.rakshabandhan.sos.ui.theme.Slate200
import kotlinx.coroutines.delay

private data class TermsSection(val title: String, val body: String)

private val termsSections = listOf(
    TermsSection(
        "1. Acceptance of Terms",
        "By downloading, installing, or using RakshaBandhan SOS (\"the App\"), you agree to be " +
                "bound by these Terms and Conditions. If you do not agree to these terms, please do " +
                "not use the App. These terms constitute a legally binding agreement between you and " +
                "RakshaBandhan Technologies Pvt. Ltd."
    ),
    TermsSection(
        "2. Description of Service",
        "RakshaBandhan SOS is a personal safety application that allows users to broadcast their " +
                "location to nearby verified responders during emergency situations. The App is " +
                "intended as a supplementary safety tool and does not replace official emergency " +
                "services. Always contact local emergency services (100, 112) in life-threatening situations."
    ),
    TermsSection(
        "3. User Eligibility",
        "You must be at least 13 years of age to use this App. By using the App, you represent " +
                "and warrant that you meet this age requirement and have the legal capacity to enter " +
                "into these Terms. Users between 13 and 18 years must have parental or guardian consent."
    ),
    TermsSection(
        "4. User Responsibilities",
        "You agree to: (a) provide accurate and complete registration information; " +
                "(b) use the SOS feature only during genuine safety emergencies; " +
                "(c) not engage in false SOS broadcasts, which may result in immediate account " +
                "termination and legal liability; (d) maintain the confidentiality of your account " +
                "credentials; and (e) comply with all applicable local laws and regulations."
    ),
    TermsSection(
        "5. Responder Code of Conduct",
        "Users who respond to SOS alerts must: (a) respond only when they are genuinely able " +
                "to assist; (b) not misuse victim location data; (c) contact official emergency " +
                "services when the situation requires professional intervention; and (d) treat " +
                "all interactions with care, dignity, and respect. Violation of this code may " +
                "result in permanent suspension from the responder programme."
    ),
    TermsSection(
        "6. Limitation of Liability",
        "RakshaBandhan Technologies Pvt. Ltd. is not liable for: (a) failure of the App to " +
                "function during network outages or device failures; (b) outcomes of SOS events " +
                "regardless of whether responders arrived; (c) actions or omissions of responders " +
                "who are independent third parties; or (d) any indirect, incidental, or consequential " +
                "damages arising from your use of the App."
    ),
    TermsSection(
        "7. Intellectual Property",
        "All content, design, code, and trademarks within the App are the exclusive property " +
                "of RakshaBandhan Technologies Pvt. Ltd. You may not reproduce, modify, distribute, " +
                "or create derivative works based on the App or its content without prior written " +
                "permission. Unauthorized use constitutes infringement of intellectual property rights."
    ),
    TermsSection(
        "8. Account Termination",
        "We reserve the right to suspend or terminate your account at our sole discretion " +
                "if you violate these Terms, engage in fraudulent or harmful behavior, or misuse " +
                "the emergency SOS system. You may also terminate your account at any time by " +
                "contacting our support team. Upon termination, your right to use the App ceases immediately."
    ),
    TermsSection(
        "9. Modifications to Terms",
        "We reserve the right to modify these Terms at any time. We will provide at least " +
                "30 days notice of material changes via email or in-app notification. Your continued " +
                "use of the App after the effective date of changes constitutes your acceptance " +
                "of the updated Terms. If you disagree with changes, you must discontinue use of the App."
    ),
    TermsSection(
        "10. Governing Law & Disputes",
        "These Terms are governed by the laws of India. Any disputes arising from or relating " +
                "to these Terms or your use of the App shall be subject to the exclusive jurisdiction " +
                "of the courts of Bengaluru, Karnataka, India. Both parties agree to attempt " +
                "good-faith resolution before initiating formal legal proceedings."
    ),
    TermsSection(
        "11. Contact Information",
        "For questions about these Terms, please contact:\n\n" +
                "Email: legal@rakshabandhan.app\n" +
                "Address: RakshaBandhan Technologies Pvt. Ltd.\n" +
                "Bengaluru, Karnataka, India 560001\n" +
                "Phone: +91 80 4567 8900"
    )
)

@Composable
fun TermsConditionsScreen(onBack: () -> Unit) {
    var visible by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) { delay(80); visible = true }

    val contentAlpha by animateFloatAsState(
        targetValue = if (visible) 1f else 0f,
        animationSpec = tween(500),
        label = "tcAlpha"
    )

    DemoFrame(
        title = "Terms & Conditions",
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
                color = Amber500.copy(alpha = 0.10f),
                border = BorderStroke(1.dp, Amber500.copy(alpha = 0.3f))
            ) {
                Row(
                    modifier = Modifier.padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Icon(Icons.Filled.Gavel, null, tint = Amber500, modifier = Modifier.size(22.dp))
                    Column {
                        Text(
                            "Please Read Carefully",
                            style = MaterialTheme.typography.titleSmall,
                            color = Slate100,
                            fontWeight = FontWeight.SemiBold
                        )
                        Text(
                            "These terms govern your use of RakshaBandhan SOS and constitute a binding agreement.",
                            style = MaterialTheme.typography.bodySmall,
                            color = Slate200
                        )
                    }
                }
            }

            // Terms sections
            termsSections.forEachIndexed { index, section ->
                TermsBlock(section = section, accent = termsAccentColor(index))
            }

            Spacer(Modifier.height(8.dp))
        }
    }
}

@Composable
private fun TermsBlock(section: TermsSection, accent: androidx.compose.ui.graphics.Color) {
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

private fun termsAccentColor(index: Int): androidx.compose.ui.graphics.Color {
    return when (index % 4) {
        0 -> Amber500
        1 -> Coral500
        2 -> Sky500
        else -> Mint500
    }
}