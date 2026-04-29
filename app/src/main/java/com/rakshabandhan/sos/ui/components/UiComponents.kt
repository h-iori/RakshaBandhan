package com.rakshabandhan.sos.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.StartOffset
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import com.rakshabandhan.sos.ui.haptics.AppHapticEvent
import com.rakshabandhan.sos.ui.haptics.hapticClickable
import com.rakshabandhan.sos.ui.haptics.withHaptic
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.FiberManualRecord
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.MyLocation
import androidx.compose.material.icons.filled.WarningAmber
import androidx.compose.material.icons.filled.ZoomIn
import androidx.compose.material.icons.filled.ZoomOut
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.rakshabandhan.sos.model.DemoScreen
import com.rakshabandhan.sos.model.ResponderItem
import com.rakshabandhan.sos.model.ResponderState
import com.rakshabandhan.sos.model.SosState
import com.rakshabandhan.sos.ui.theme.Amber500
import com.rakshabandhan.sos.ui.theme.Coral500
import com.rakshabandhan.sos.ui.theme.Mint500
import com.rakshabandhan.sos.ui.theme.Sky500

import com.rakshabandhan.sos.ui.theme.LocalThemeMode

// ── AppBackground ─────────────────────────────────────────────────────────────

@Composable
fun AppBackground(modifier: Modifier = Modifier, content: @Composable () -> Unit) {
    val bg = MaterialTheme.colorScheme.background
    val surfaceVar = MaterialTheme.colorScheme.surfaceVariant
    val gradientColors = listOf(bg, surfaceVar, bg)
    
    Box(
        modifier = modifier.background(
            Brush.linearGradient(gradientColors)
        )
    ) { content() }
}

// ── DemoFrame ─────────────────────────────────────────────────────────────────

@Composable
fun DemoFrame(
    title: String,
    subtitle: String,
    modifier: Modifier = Modifier,
    trailing: @Composable (() -> Unit)? = null,
    content: @Composable () -> Unit
) {
    ElevatedCard(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.elevatedCardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 14.dp),
        shape = MaterialTheme.shapes.large
    ) {
        Column(modifier = Modifier.padding(18.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(title, style = MaterialTheme.typography.titleLarge, color = MaterialTheme.colorScheme.onSurface)
                    if (subtitle.isNotBlank()) {
                        Spacer(Modifier.height(4.dp))
                        Text(subtitle, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                }
                trailing?.invoke()
            }
            Spacer(Modifier.height(18.dp))
            content()
        }
    }
}

// ── HeroMetric ────────────────────────────────────────────────────────────────

@Composable
fun HeroMetric(
    value: String,
    label: String,
    modifier: Modifier = Modifier,
    accentColor: Color = Mint500
) {
    Surface(
        modifier = modifier,
        shape = MaterialTheme.shapes.medium,
        color = accentColor.copy(alpha = 0.08f),
        border = BorderStroke(1.dp, accentColor.copy(alpha = 0.22f))
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Text(value, style = MaterialTheme.typography.headlineLarge, color = MaterialTheme.colorScheme.onSurface, fontWeight = FontWeight.Bold)
            Spacer(Modifier.height(2.dp))
            Text(label, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }
}

// ── StatusChip with pulsing dot ───────────────────────────────────────────────

@Composable
fun StatusChip(text: String, state: SosState, modifier: Modifier = Modifier) {
    val color = when (state) {
        SosState.ACTIVE -> Mint500
        SosState.ENDING -> Amber500
        SosState.STOPPED -> Mint500
    }
    val infinite = rememberInfiniteTransition(label = "chip")
    val dotAlpha by infinite.animateFloat(
        initialValue = 1f, targetValue = 0.15f,
        animationSpec = infiniteRepeatable(tween(750, easing = FastOutSlowInEasing), RepeatMode.Reverse),
        label = "dotAlpha"
    )
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(999.dp),
        color = color.copy(alpha = 0.14f),
        border = BorderStroke(1.dp, color.copy(alpha = 0.45f))
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)
        ) {
            Icon(
                Icons.Filled.FiberManualRecord, null, tint = color,
                modifier = Modifier.size(10.dp).graphicsLayer { alpha = if (state == SosState.ACTIVE) dotAlpha else 1f }
            )
            Spacer(Modifier.width(8.dp))
            Text(text, style = MaterialTheme.typography.labelLarge, color = MaterialTheme.colorScheme.onSurface)
        }
    }
}

// ── PrimarySosButton – sonar rings + breathe ─────────────────────────────────

@Composable
fun PrimarySosButton(onClick: () -> Unit, modifier: Modifier = Modifier) {
    val infinite = rememberInfiniteTransition(label = "sos")

    val ring1 by infinite.animateFloat(
        initialValue = 0f, targetValue = 1f,
        animationSpec = infiniteRepeatable(tween(2200, easing = LinearEasing), RepeatMode.Restart, StartOffset(0)),
        label = "r1"
    )
    val ring2 by infinite.animateFloat(
        initialValue = 0f, targetValue = 1f,
        animationSpec = infiniteRepeatable(tween(2200, easing = LinearEasing), RepeatMode.Restart, StartOffset(733)),
        label = "r2"
    )
    val ring3 by infinite.animateFloat(
        initialValue = 0f, targetValue = 1f,
        animationSpec = infiniteRepeatable(tween(2200, easing = LinearEasing), RepeatMode.Restart, StartOffset(1466)),
        label = "r3"
    )
    val breathe by infinite.animateFloat(
        initialValue = 1f, targetValue = 1.05f,
        animationSpec = infiniteRepeatable(tween(900, easing = FastOutSlowInEasing), RepeatMode.Reverse),
        label = "breathe"
    )

    Box(modifier = modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
        Canvas(modifier = Modifier.fillMaxWidth().height(96.dp)) {
            val center = Offset(size.width / 2f, size.height / 2f)
            val maxR = size.minDimension * 1.05f
            listOf(ring1, ring2, ring3).forEach { p ->
                if (p > 0f) {
                    drawCircle(
                        color = Coral500.copy(alpha = (1f - p) * 0.5f),
                        radius = (maxR * p * 0.82f).coerceAtLeast(1f),
                        center = center,
                        style = Stroke(width = 2.5f)
                    )
                }
            }
        }
        Button(
            onClick = withHaptic(AppHapticEvent.HEAVY_CLICK, onClick),
            modifier = Modifier
                .fillMaxWidth(0.88f)
                .height(64.dp)
                .graphicsLayer {
                    scaleX = breathe
                    scaleY = breathe
                }
                .shadow(
                    24.dp, MaterialTheme.shapes.large, clip = false,
                    ambientColor = Coral500.copy(alpha = 0.5f),
                    spotColor = Coral500.copy(alpha = 0.5f)
                ),
            shape = MaterialTheme.shapes.large,
            colors = ButtonDefaults.buttonColors(containerColor = Coral500, contentColor = MaterialTheme.colorScheme.onPrimary)
        ) {
            Text(
                "SOS",
                style = MaterialTheme.typography.headlineLarge.copy(
                    letterSpacing = 8.sp,
                    fontWeight = FontWeight.ExtraBold
                )
            )
        }
    }
}

// ── MapPlaceholderCard – tap to expand fullscreen ─────────────────────────────

@Composable
fun MapPlaceholderCard(
    title: String,
    subtitle: String,
    modifier: Modifier = Modifier,
    showRoute: Boolean = false,
    compactFullscreenFooter: Boolean = false,
    fullscreenStatusLabel: String = "Recorded",
    footerText: String? = null,
    fullscreenStats: List<MapFooterStat>? = null,
    fullscreenLocationLabel: String? = "MG Road, Bengaluru"
) {
    var expanded by remember { mutableStateOf(false) }
    val infinite = rememberInfiniteTransition(label = "map")
    val pinPulse by infinite.animateFloat(
        initialValue = 0f, targetValue = 1f,
        animationSpec = infiniteRepeatable(tween(2000, easing = FastOutSlowInEasing), RepeatMode.Restart),
        label = "pin"
    )
    val dashOff by infinite.animateFloat(
        initialValue = 0f, targetValue = 40f,
        animationSpec = infiniteRepeatable(tween(1600, easing = LinearEasing), RepeatMode.Restart),
        label = "dash"
    )

    val surface = MaterialTheme.colorScheme.surface
    val surfaceVar = MaterialTheme.colorScheme.surfaceVariant
    val onSurface = MaterialTheme.colorScheme.onSurface
    val outline = MaterialTheme.colorScheme.outline

    ElevatedCard(
        modifier = modifier.fillMaxWidth().aspectRatio(1.35f).hapticClickable(hapticEvent = AppHapticEvent.TAP) { expanded = true },
        shape = MaterialTheme.shapes.large,
        colors = CardDefaults.elevatedCardColors(containerColor = surface),
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 12.dp)
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            Box(modifier = Modifier.fillMaxSize().background(
                Brush.radialGradient(
                    listOf(surfaceVar, surface),
                    radius = 900f
                )
            ))
            val gridColor = onSurface.copy(alpha = 0.08f)
            Canvas(modifier = Modifier.fillMaxSize()) {
                val step = size.minDimension / 7f
                for (i in 1..6) {
                    val v = step * i
                    drawLine(gridColor, Offset(v, 0f), Offset(v, size.height), 1f)
                    drawLine(gridColor, Offset(0f, v), Offset(size.width, v), 1f)
                }
                if (showRoute) {
                    val path = Path().apply {
                        moveTo(size.width * 0.2f, size.height * 0.78f)
                        cubicTo(size.width * 0.35f, size.height * 0.3f, size.width * 0.5f, size.height * 0.6f, size.width * 0.68f, size.height * 0.38f)
                    }
                    drawPath(path, Sky500.copy(alpha = 0.6f), style = Stroke(3.5f, cap = StrokeCap.Round,
                        pathEffect = androidx.compose.ui.graphics.PathEffect.dashPathEffect(floatArrayOf(14f, 8f), dashOff)))
                    drawCircle(Sky500.copy(alpha = 0.9f), 9f, Offset(size.width * 0.2f, size.height * 0.78f))
                }
                listOf(
                    Offset(size.width * 0.18f, size.height * 0.22f),
                    Offset(size.width * 0.44f, size.height * 0.64f),
                    Offset(size.width * 0.82f, size.height * 0.70f),
                ).forEach { pos ->
                    drawCircle(Mint500.copy(alpha = 0.8f), 7f, pos)
                    drawCircle(Mint500.copy(alpha = 0.18f), 15f, pos, style = Stroke(1.5f))
                }
                val pin = Offset(size.width * 0.68f, size.height * 0.38f)
                drawCircle(Coral500.copy(alpha = (1f - pinPulse) * 0.45f), (step * 0.3f + pinPulse * step * 1.1f).coerceAtLeast(1f), pin, style = Stroke(3f))
                drawCircle(Sky500.copy(alpha = 0.95f), step * 0.27f, pin)
                drawCircle(surface, step * 0.11f, pin)
            }
            Column(modifier = Modifier.align(Alignment.TopStart).padding(14.dp)) {
                Text(title, style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.onSurface)
                Text(subtitle, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
            if (!footerText.isNullOrBlank()) {
                Row(modifier = Modifier.align(Alignment.BottomStart).padding(14.dp), verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Filled.LocationOn, null, tint = Coral500, modifier = Modifier.size(15.dp))
                    Spacer(Modifier.width(5.dp))
                    Text(footerText, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }
            Surface(
                modifier = Modifier.align(Alignment.TopEnd).padding(10.dp).size(30.dp),
                shape = CircleShape,
                color = surface.copy(alpha = 0.4f),
                border = BorderStroke(1.dp, outline.copy(alpha = 0.2f))
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(Icons.Filled.ZoomIn, null, tint = MaterialTheme.colorScheme.onSurfaceVariant, modifier = Modifier.size(15.dp))
                }
            }
        }
    }

    if (expanded) {
        Dialog(
            onDismissRequest = { expanded = false },
            properties = DialogProperties(usePlatformDefaultWidth = false, dismissOnBackPress = true)
        ) {
            FullscreenMapOverlay(
                title = title,
                subtitle = subtitle,
                showRoute = showRoute,
                compactFooter = compactFullscreenFooter,
                statusLabel = fullscreenStatusLabel,
                fullscreenStats = fullscreenStats,
                fullscreenLocationLabel = fullscreenLocationLabel,
                onClose = { expanded = false }
            )
        }
    }
}

data class MapFooterStat(
    val value: String,
    val label: String,
    val color: Color
)

@Composable
private fun FullscreenMapOverlay(
    title: String,
    subtitle: String,
    showRoute: Boolean,
    compactFooter: Boolean,
    statusLabel: String,
    fullscreenStats: List<MapFooterStat>?,
    fullscreenLocationLabel: String?,
    onClose: () -> Unit
) {
    val infinite = rememberInfiniteTransition(label = "fsmap")
    val pinPulse by infinite.animateFloat(
        initialValue = 0f, targetValue = 1f,
        animationSpec = infiniteRepeatable(tween(1800), RepeatMode.Restart), label = "fsp"
    )
    val dashOff by infinite.animateFloat(
        initialValue = 0f, targetValue = 40f,
        animationSpec = infiniteRepeatable(tween(1600, easing = LinearEasing), RepeatMode.Restart), label = "fsd"
    )

    val mapBgColor = MaterialTheme.colorScheme.background
    val gridLineColor = MaterialTheme.colorScheme.onSurface
    val surface = MaterialTheme.colorScheme.surface
    Box(modifier = Modifier.fillMaxSize().background(mapBgColor)) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val step = size.minDimension / 11f
            var x = 0f; while (x < size.width) { drawLine(gridLineColor.copy(alpha = 0.07f), Offset(x, 0f), Offset(x, size.height), 1f); x += step }
            var y = 0f; while (y < size.height) { drawLine(gridLineColor.copy(alpha = 0.07f), Offset(0f, y), Offset(size.width, y), 1f); y += step }
            drawLine(gridLineColor.copy(alpha = 0.12f), Offset(0f, size.height * 0.5f), Offset(size.width, size.height * 0.5f), 2.5f)
            drawLine(gridLineColor.copy(alpha = 0.12f), Offset(size.width * 0.4f, 0f), Offset(size.width * 0.4f, size.height), 2.5f)
            if (showRoute) {
                val path = Path().apply {
                    moveTo(size.width * 0.18f, size.height * 0.80f)
                    cubicTo(size.width * 0.3f, size.height * 0.4f, size.width * 0.55f, size.height * 0.6f, size.width * 0.70f, size.height * 0.38f)
                }
                drawPath(path, Sky500.copy(alpha = 0.75f), style = Stroke(4.5f, cap = StrokeCap.Round,
                    pathEffect = androidx.compose.ui.graphics.PathEffect.dashPathEffect(floatArrayOf(18f, 10f), dashOff)))
                drawCircle(Sky500, 12f, Offset(size.width * 0.18f, size.height * 0.80f))
            }
            listOf(
                Pair(Offset(size.width * 0.15f, size.height * 0.20f), Mint500),
                Pair(Offset(size.width * 0.45f, size.height * 0.68f), Sky500),
                Pair(Offset(size.width * 0.83f, size.height * 0.73f), Mint500),
                Pair(Offset(size.width * 0.25f, size.height * 0.55f), Amber500),
            ).forEach { (pos, col) ->
                drawCircle(col.copy(alpha = 0.85f), 11f, pos)
                drawCircle(col.copy(alpha = 0.22f), 22f, pos, style = Stroke(2f))
            }
            val pin = Offset(size.width * 0.70f, size.height * 0.38f)
            drawCircle(Coral500.copy(alpha = (1f - pinPulse) * 0.5f), (step * 0.5f + pinPulse * step * 2.2f).coerceAtLeast(1f), pin, style = Stroke(3.5f))
            drawCircle(Sky500, step * 0.38f, pin)
            drawCircle(surface, step * 0.16f, pin)
        }
        // Top gradient
        Box(modifier = Modifier.fillMaxWidth().height(140.dp).background(Brush.verticalGradient(listOf(MaterialTheme.colorScheme.background.copy(alpha=0.88f), Color.Transparent))))
        // Bottom gradient
        Box(modifier = Modifier.fillMaxWidth().height(140.dp).align(Alignment.BottomCenter).background(Brush.verticalGradient(listOf(Color.Transparent, MaterialTheme.colorScheme.background.copy(alpha=0.88f)))))

        // Top bar
        Row(
            modifier = Modifier.fillMaxWidth().statusBarsPadding().padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(title, style = MaterialTheme.typography.titleLarge, color = MaterialTheme.colorScheme.onSurface)
                Text(subtitle, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
            FilledTonalIconButton(onClick = withHaptic(AppHapticEvent.REJECT, onClose), colors = IconButtonDefaults.filledTonalIconButtonColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant, contentColor = MaterialTheme.colorScheme.onSurface
            )) { Icon(Icons.Filled.Close, null) }
        }

        // Bottom info strip
        Surface(
            modifier = Modifier.fillMaxWidth().align(Alignment.BottomCenter).navigationBarsPadding().padding(16.dp),
            shape = MaterialTheme.shapes.large,
            color = MaterialTheme.colorScheme.surface,
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
        ) {
            Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                if (compactFooter) {
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceAround) {
                        FsMapStat("500m", "Radius", Coral500)
                        FsMapStat(
                            statusLabel,
                            "Record",
                            Mint500
                        )
                    }
                } else {
                    val footerStats = (fullscreenStats ?: listOf(
                        MapFooterStat("500m", "Radius", Coral500),
                        MapFooterStat("3", "Responders", Mint500),
                        MapFooterStat("4", "Arrived", Amber500)
                    )).filterNot { stat ->
                        stat.label.contains("eta", ignoreCase = true)
                    }
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceAround) {
                        footerStats.forEach { stat ->
                            FsMapStat(stat.value, stat.label, stat.color)
                        }
                    }
                    if (!fullscreenLocationLabel.isNullOrBlank()) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Filled.MyLocation, null, tint = Coral500, modifier = Modifier.size(13.dp))
                            Spacer(Modifier.width(6.dp))
                            Text(fullscreenLocationLabel, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                    }
                }
            }
        }

        // Zoom controls
        Column(modifier = Modifier.align(Alignment.CenterEnd).padding(end = 14.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            MapCtrlBtn { Icon(Icons.Filled.ZoomIn, null, tint = MaterialTheme.colorScheme.onSurface) }
            MapCtrlBtn { Icon(Icons.Filled.ZoomOut, null, tint = MaterialTheme.colorScheme.onSurface) }
        }
    }
}

@Composable private fun FsMapStat(value: String, label: String, color: Color) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(value, style = MaterialTheme.typography.titleSmall, color = color, fontWeight = FontWeight.Bold)
        Text(label, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
    }
}

@Composable private fun MapCtrlBtn(icon: @Composable () -> Unit) {
    Surface(modifier = Modifier.size(36.dp), shape = RoundedCornerShape(10.dp), color = MaterialTheme.colorScheme.surfaceVariant, border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)) {
        Box(contentAlignment = Alignment.Center) { icon() }
    }
}

// ── SecondaryActionButton ─────────────────────────────────────────────────────

@Composable
fun SecondaryActionButton(label: String, onClick: () -> Unit, modifier: Modifier = Modifier) {
    OutlinedButton(
        onClick = withHaptic(AppHapticEvent.TAP, onClick), modifier = modifier.height(52.dp),
        shape = MaterialTheme.shapes.medium,
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
        colors = ButtonDefaults.outlinedButtonColors(contentColor = MaterialTheme.colorScheme.onSurface)
    ) { Text(label, style = MaterialTheme.typography.labelLarge) }
}

// ── LinearMetricRow ───────────────────────────────────────────────────────────

@Composable
fun LinearMetricRow(title: String, value: String, modifier: Modifier = Modifier) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.medium,
        color = MaterialTheme.colorScheme.surface,
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 13.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(title, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
            Text(value, style = MaterialTheme.typography.titleSmall, color = MaterialTheme.colorScheme.onSurface, fontWeight = FontWeight.SemiBold)
        }
    }
}

// ── ResponderCard – staggered slide-in ───────────────────────────────────────

@Composable
fun ResponderCard(responder: ResponderItem, modifier: Modifier = Modifier, animIndex: Int = 0) {
    var visible by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) {
        kotlinx.coroutines.delay(animIndex * 90L)
        visible = true
    }
    AnimatedVisibility(
        visible = visible,
        enter = slideInVertically(
            initialOffsetY = { it },
            animationSpec = spring(Spring.DampingRatioMediumBouncy, Spring.StiffnessMedium)
        ) + fadeIn()
    ) {
        val accent = when (responder.state) {
            ResponderState.COMING -> Sky500
            ResponderState.ARRIVED -> Mint500
            ResponderState.NONE -> MaterialTheme.colorScheme.onSurfaceVariant
        }
        Surface(
            modifier = modifier.fillMaxWidth(),
            shape = MaterialTheme.shapes.medium,
            color = MaterialTheme.colorScheme.surface,
            border = BorderStroke(1.dp, accent.copy(alpha = 0.28f))
        ) {
            Row(modifier = Modifier.padding(14.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                Surface(shape = RoundedCornerShape(12.dp), color = accent.copy(alpha = 0.16f), modifier = Modifier.size(44.dp)) {
                    Box(contentAlignment = Alignment.Center) {
                        Text(responder.avatar, style = MaterialTheme.typography.titleMedium, color = accent, fontWeight = FontWeight.Bold)
                    }
                }
                Column(modifier = Modifier.weight(1f)) {
                    Text(responder.name, style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.onSurface)
                    Text("${responder.distanceMeters}m away", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
                Surface(shape = RoundedCornerShape(999.dp), color = accent.copy(alpha = 0.12f)) {
                    Text(
                        when (responder.state) {
                            ResponderState.COMING -> "On the way"; ResponderState.ARRIVED -> "Arrived"; ResponderState.NONE -> "Nearby"
                        },
                        style = MaterialTheme.typography.labelSmall, color = accent,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp)
                    )
                }
            }
        }
    }
}

// ── TimelineCard – staggered fade-up ─────────────────────────────────────────

@Composable
fun TimelineCard(time: String, title: String, detail: String, modifier: Modifier = Modifier, animIndex: Int = 0) {
    var visible by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) {
        kotlinx.coroutines.delay(animIndex * 100L)
        visible = true
    }
    AnimatedVisibility(
        visible = visible,
        enter = slideInVertically(
            initialOffsetY = { it / 2 },
            animationSpec = spring(Spring.DampingRatioLowBouncy, Spring.StiffnessMediumLow)
        ) + fadeIn(tween(300))
    ) {
        Surface(
            modifier = modifier.fillMaxWidth(),
            shape = MaterialTheme.shapes.medium,
            color = MaterialTheme.colorScheme.surface,
            border = BorderStroke(1.dp, Mint500.copy(alpha = 0.15f))
        ) {
            Row(modifier = Modifier.padding(14.dp), verticalAlignment = Alignment.Top) {
                Box(modifier = Modifier.size(32.dp).background(Mint500.copy(alpha = 0.12f), CircleShape), contentAlignment = Alignment.Center) {
                    Icon(Icons.Filled.CheckCircle, null, tint = Mint500, modifier = Modifier.size(18.dp))
                }
                Spacer(Modifier.width(12.dp))
                Column {
                    Text(time, style = MaterialTheme.typography.labelSmall, color = Amber500)
                    Spacer(Modifier.height(2.dp))
                    Text(title, style = MaterialTheme.typography.titleSmall, color = MaterialTheme.colorScheme.onSurface, fontWeight = FontWeight.SemiBold)
                    Text(detail, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }
        }
    }
}

// ── DemoDivider ───────────────────────────────────────────────────────────────

@Composable
fun DemoDivider() {
    Surface(color = MaterialTheme.colorScheme.outlineVariant, modifier = Modifier.fillMaxWidth().height(1.dp)) {}
}

// ── PulsingHighlight ──────────────────────────────────────────────────────────

@Composable
fun PulsingHighlight(modifier: Modifier = Modifier) {
    val infinite = rememberInfiniteTransition(label = "warn")
    val scale by infinite.animateFloat(
        initialValue = 0.88f, targetValue = 1.18f,
        animationSpec = infiniteRepeatable(tween(700, easing = FastOutSlowInEasing), RepeatMode.Reverse),
        label = "ws"
    )
    Icon(Icons.Filled.WarningAmber, null, tint = Coral500, modifier = modifier.size(28.dp).graphicsLayer {
        scaleX = scale
        scaleY = scale
    })
}

// ── DemoScreenTabs ────────────────────────────────────────────────────────────

@Composable
fun DemoScreenTabs(selected: DemoScreen, onSelect: (DemoScreen) -> Unit, modifier: Modifier = Modifier) {
    val tabs = listOf(DemoScreen.AUTH, DemoScreen.HOME, DemoScreen.ACTIVE_SOS, DemoScreen.RESPONDER_ALERT, DemoScreen.HISTORY)
    Row(modifier = modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        tabs.forEach { screen ->
            val isSelected = screen == selected
            val bg by animateColorAsState(if (isSelected) Coral500 else Color.Transparent, label = "tabBg")
            Surface(
                shape = RoundedCornerShape(999.dp), color = bg,
                border = BorderStroke(1.dp, if (isSelected) Coral500 else MaterialTheme.colorScheme.outline),
                modifier = Modifier.weight(1f).clip(RoundedCornerShape(999.dp)).hapticClickable(hapticEvent = AppHapticEvent.SELECTION) { onSelect(screen) }
            ) {
                Text(
                    screen.name.take(4),
                    style = MaterialTheme.typography.labelSmall,
                    color = if (isSelected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 8.dp),
                    maxLines = 1
                )
            }
        }
    }
}
