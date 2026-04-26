package com.rakshabandhan.sos.ui.screens

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Code
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Shield
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
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.layout.ContentScale
import com.rakshabandhan.sos.ui.components.DemoFrame
import com.rakshabandhan.sos.ui.haptics.AppHapticEvent
import com.rakshabandhan.sos.ui.haptics.withHaptic
import com.rakshabandhan.sos.ui.theme.Amber500
import com.rakshabandhan.sos.ui.theme.Coral500
import com.rakshabandhan.sos.ui.theme.Mint500
import com.rakshabandhan.sos.ui.theme.Navy800
import com.rakshabandhan.sos.ui.theme.Sky500
import com.rakshabandhan.sos.ui.theme.Slate100
import com.rakshabandhan.sos.ui.theme.Slate200
import com.rakshabandhan.sos.ui.theme.Slate700
import kotlinx.coroutines.delay
import kotlin.math.cos
import kotlin.math.sin
import com.rakshabandhan.sos.R
@Composable
fun AboutScreen(onBack: () -> Unit) {

    // ── Staggered entrance fade-in flags ─────────────────────────────────────
    var avatarVisible by remember { mutableStateOf(false) }
    var nameVisible by remember { mutableStateOf(false) }
    var bioVisible by remember { mutableStateOf(false) }
    var techVisible by remember { mutableStateOf(false) }
    var contactVisible by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        delay(100); avatarVisible = true
        delay(300); nameVisible = true
        delay(300); bioVisible = true
        delay(200); techVisible = true
        delay(200); contactVisible = true
    }

    // ── Continuous infinite animations ────────────────────────────────────────
    val infinite = rememberInfiniteTransition(label = "about")

    val ringRotation by infinite.animateFloat(
        initialValue = 0f, targetValue = 360f,
        animationSpec = infiniteRepeatable(tween(8000, easing = LinearEasing), RepeatMode.Restart),
        label = "ring"
    )
    val glowAlpha by infinite.animateFloat(
        initialValue = 0.25f, targetValue = 0.65f,
        animationSpec = infiniteRepeatable(tween(1500, easing = FastOutSlowInEasing), RepeatMode.Reverse),
        label = "glow"
    )
    val avatarScale by infinite.animateFloat(
        initialValue = 1f, targetValue = 1.04f,
        animationSpec = infiniteRepeatable(tween(1800, easing = FastOutSlowInEasing), RepeatMode.Reverse),
        label = "avatarScale"
    )
    val particleFloat by infinite.animateFloat(
        initialValue = 0f, targetValue = 10f,
        animationSpec = infiniteRepeatable(tween(2200, easing = FastOutSlowInEasing), RepeatMode.Reverse),
        label = "particle"
    )

    // ── Fade-in alpha for each section ────────────────────────────────────────
    val avatarAlpha by animateFloatAsState(
        targetValue = if (avatarVisible) 1f else 0f,
        animationSpec = tween(600), label = "aA"
    )
    val nameAlpha by animateFloatAsState(
        targetValue = if (nameVisible) 1f else 0f,
        animationSpec = tween(600), label = "nA"
    )
    val bioAlpha by animateFloatAsState(
        targetValue = if (bioVisible) 1f else 0f,
        animationSpec = tween(700), label = "bA"
    )
    val techAlpha by animateFloatAsState(
        targetValue = if (techVisible) 1f else 0f,
        animationSpec = tween(700), label = "tA"
    )
    val contactAlpha by animateFloatAsState(
        targetValue = if (contactVisible) 1f else 0f,
        animationSpec = tween(700), label = "cA"
    )

    DemoFrame(
        title = "About",
        subtitle = "Meet the developer.",
        trailing = {
            IconButton(onClick = withHaptic(AppHapticEvent.TAP, onBack)) {
                Icon(Icons.Filled.ArrowBack, contentDescription = "Back", tint = Slate200)
            }
        }
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(24.dp),
            modifier = Modifier.fillMaxWidth()
        ) {

            // ── Developer avatar with animated rings ─────────────────────────
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .alpha(avatarAlpha)
                    .scale(avatarScale)
            ) {
                // Rotating arc ring
                Canvas(
                    modifier = Modifier
                        .size(160.dp)
                        .rotate(ringRotation)
                ) {
                    drawArc(
                        brush = Brush.sweepGradient(
                            listOf(
                                Coral500.copy(alpha = 0f),
                                Coral500.copy(alpha = 0.9f),
                                Coral500.copy(alpha = 0f)
                            )
                        ),
                        startAngle = 0f,
                        sweepAngle = 240f,
                        useCenter = false,
                        style = Stroke(width = 3f, cap = StrokeCap.Round)
                    )
                }

                // Pulsing glow ring
                Canvas(modifier = Modifier.size(136.dp)) {
                    drawCircle(
                        color = Coral500.copy(alpha = glowAlpha * 0.25f),
                        radius = size.minDimension / 2f,
                        style = Stroke(width = 22f)
                    )
                }

                // Floating orbital dots
                Canvas(modifier = Modifier.size(160.dp)) {
                    val cx = size.width / 2f
                    val cy = size.height / 2f
                    val orbitRadius = size.minDimension / 2f - 8f
                    val dotColors = listOf(Coral500, Mint500, Sky500, Amber500)
                    val baseAngles = listOf(0f, 90f, 180f, 270f)
                    baseAngles.forEachIndexed { i, baseAngle ->
                        val angle = baseAngle + ringRotation * 0.4f
                        val rad = Math.toRadians(angle.toDouble())
                        val floatOffset = if (i % 2 == 0) particleFloat else -particleFloat
                        val px = cx + orbitRadius * cos(rad).toFloat()
                        val py = cy + orbitRadius * sin(rad).toFloat() + floatOffset * 0.2f
                        drawCircle(
                            color = dotColors[i],
                            radius = 5.5f,
                            center = Offset(px, py)
                        )
                    }
                }

                // Developer photo circle (placeholder — swap with Image when you have a real photo)
                Surface(
                    shape = CircleShape,
                    color = Color(0xFF1A3362),
                    border = BorderStroke(3.dp, Coral500.copy(alpha = 0.6f)),
                    modifier = Modifier.size(118.dp)
                ) {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier.background(
                            Brush.radialGradient(
                                listOf(Color(0xFF1E3E70), Color(0xFF0D1C34))
                            )
                        )
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.dev_profile), 
                            contentDescription = "Profile photo of Harsh Swatantra Upadhyay",
                            contentScale = ContentScale.Crop, // Ensures the image fills the circle without stretching
                            modifier = Modifier
                                .fillMaxSize()
                                .clip(CircleShape)
                        )
                    }
                }
            }

            // ── Name & title ─────────────────────────────────────────────────
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.alpha(nameAlpha)
            ) {
                Text(
                    "Harsh Swatantra Upadhyay",
                    style = MaterialTheme.typography.headlineMedium,
                    color = Slate100,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )
                Spacer(Modifier.height(4.dp))
                Text(
                    "AI Engineer • Tech Enthusiast",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Coral500,
                    textAlign = TextAlign.Center
                )
                Spacer(Modifier.height(2.dp))
                Text(
                    "📍 Mumbai, India",
                    style = MaterialTheme.typography.bodySmall,
                    color = Slate200,
                    textAlign = TextAlign.Center
                )
            }

            // ── Bio ──────────────────────────────────────────────────────────
            Surface(
                shape = MaterialTheme.shapes.large,
                color = Navy800,
                border = BorderStroke(1.dp, Slate700.copy(alpha = 0.5f)),
                modifier = Modifier
                    .fillMaxWidth()
                    .alpha(bioAlpha)
            ) {
                Column(
                    modifier = Modifier.padding(18.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(
                            Icons.Filled.Favorite,
                            contentDescription = null,
                            tint = Coral500,
                            modifier = Modifier.size(18.dp)
                        )
                        Text(
                            "About Me",
                            style = MaterialTheme.typography.titleMedium,
                            color = Slate100,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                    Text(
                        "I am an AI Engineer and Tech Enthusiast based in Mumbai, India. " +
                            "RakshaBandhan SOS was independently designed and developed end to end by me, " +
                            "with a focus on clean architecture, reliability, and a polished user experience. " +
                            "I build practical, production-minded software with a strong emphasis on purpose, precision, and quality.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Slate200
                    )
                }
            }


            // ── App info ─────────────────────────────────────────────────────
            Surface(
                shape = MaterialTheme.shapes.large,
                color = Navy800,
                border = BorderStroke(1.dp, Slate700.copy(alpha = 0.5f)),
                modifier = Modifier
                    .fillMaxWidth()
                    .alpha(contactAlpha)
            ) {
                Column(
                    modifier = Modifier.padding(18.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(
                            Icons.Filled.Shield,
                            contentDescription = null,
                            tint = Coral500,
                            modifier = Modifier.size(18.dp)
                        )
                        Text(
                            "App Info",
                            style = MaterialTheme.typography.titleMedium,
                            color = Slate100,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                    AboutInfoRow("App Name", "RakshaBandhan")
                    AboutInfoRow("Version", "1.1")
                    AboutInfoRow("Contact", "harshupadhyay9702@gmail.com")
                    AboutInfoRow("GitHub", "https://www.github.com/h-iori")
                }
            }

            Text(
                "Built independently with 💪 purpose",
                style = MaterialTheme.typography.bodySmall,
                color = Slate200.copy(alpha = 0.5f),
                textAlign = TextAlign.Center,
                modifier = Modifier.alpha(contactAlpha)
            )

            Spacer(Modifier.height(8.dp))
        }
    }
}

// ── Private helpers ───────────────────────────────────────────────────────────

@Composable
private fun TechRow(label: String, value: String, accent: Color) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(label, style = MaterialTheme.typography.bodySmall, color = Slate200)
        Surface(
            shape = MaterialTheme.shapes.extraSmall,
            color = accent.copy(alpha = 0.12f),
            border = BorderStroke(1.dp, accent.copy(alpha = 0.3f))
        ) {
            Text(
                value,
                style = MaterialTheme.typography.labelSmall,
                color = accent,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
            )
        }
    }
}

@Composable
private fun AboutInfoRow(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(label, style = MaterialTheme.typography.bodySmall, color = Slate200)
        Text(
            value,
            style = MaterialTheme.typography.bodySmall,
            color = Slate100,
            fontWeight = FontWeight.Medium
        )
    }
}