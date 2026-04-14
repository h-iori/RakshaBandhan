package com.rakshabandhan.sos.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
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
import androidx.compose.material.icons.filled.DirectionsWalk
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.NotificationsActive
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
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
import androidx.compose.ui.draw.scale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.rakshabandhan.sos.model.demoResponders
import com.rakshabandhan.sos.ui.components.DemoFrame
import com.rakshabandhan.sos.ui.components.HeroMetric
import com.rakshabandhan.sos.ui.components.MapPlaceholderCard
import com.rakshabandhan.sos.ui.components.ResponderCard
import com.rakshabandhan.sos.ui.theme.Coral500
import com.rakshabandhan.sos.ui.theme.Mint500
import com.rakshabandhan.sos.ui.theme.Navy900
import com.rakshabandhan.sos.ui.theme.Slate100
import com.rakshabandhan.sos.ui.theme.Slate200
import com.rakshabandhan.sos.ui.theme.Slate700
import kotlinx.coroutines.delay

@Composable
fun ResponderAlertScreen() {
    val visible = remember { Array(5) { mutableStateOf(false) } }
    LaunchedEffect(Unit) {
        visible.forEachIndexed { i, s -> delay(i * 90L); s.value = true }
    }

    @Composable fun Slot(i: Int, content: @Composable () -> Unit) {
        AnimatedVisibility(
            visible = visible[i].value,
            enter = slideInVertically(
                initialOffsetY = { it / 2 },
                animationSpec = spring(Spring.DampingRatioMediumBouncy, Spring.StiffnessMedium)
            ) + fadeIn(tween(240))
        ) { content() }
    }

    // Urgent pulsing alert banner
    val infinite = rememberInfiniteTransition(label = "alert")
    val alertScale by infinite.animateFloat(
        initialValue = 1f, targetValue = 1.025f,
        animationSpec = infiniteRepeatable(tween(600, easing = FastOutSlowInEasing), RepeatMode.Reverse),
        label = "alertScale"
    )

    DemoFrame(
        title = "Responder alert",
        subtitle = "Someone nearby needs help now.",
        trailing = { Icon(Icons.Filled.NotificationsActive, null, tint = Coral500) }
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {

            // Urgent banner
            Surface(
                modifier = Modifier.fillMaxWidth().scale(alertScale),
                shape = MaterialTheme.shapes.medium,
                color = Coral500.copy(alpha = 0.1f),
                border = BorderStroke(1.5.dp, Coral500.copy(alpha = 0.5f))
            ) {
                Row(
                    modifier = Modifier.padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Icon(Icons.Filled.LocationOn, null, tint = Coral500, modifier = Modifier.size(22.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text("SOS Alert • MG Road, Bengaluru", style = MaterialTheme.typography.titleSmall, color = Slate100, fontWeight = FontWeight.Bold)
                        Text("430m from your location", style = MaterialTheme.typography.bodySmall, color = Slate200)
                    }
                }
            }

            Slot(0) {
                MapPlaceholderCard(
                    title = "Victim live location",
                    subtitle = "Tap to expand full map.",
                    showRoute = true
                )
            }

            Slot(1) {
                Row(horizontalArrangement = Arrangement.spacedBy(12.dp), modifier = Modifier.fillMaxWidth()) {
                    HeroMetric("430m", "Distance to scene", modifier = Modifier.weight(1f), accentColor = Coral500)
                    HeroMetric("4 min", "Your ETA", modifier = Modifier.weight(1f), accentColor = Mint500)
                }
            }

            Slot(2) {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text("Respond", style = MaterialTheme.typography.titleMedium, color = Slate100, fontWeight = FontWeight.SemiBold)
                    Row(horizontalArrangement = Arrangement.spacedBy(10.dp), modifier = Modifier.fillMaxWidth()) {
                        Button(
                            onClick = {},
                            modifier = Modifier.weight(1f).height(52.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = Coral500, contentColor = Navy900)
                        ) {
                            Icon(Icons.Filled.DirectionsWalk, null, modifier = Modifier.size(18.dp))
                            Spacer(Modifier.width(6.dp))
                            Text("I am coming", fontWeight = FontWeight.Bold)
                        }
                        Button(
                            onClick = {},
                            modifier = Modifier.weight(1f).height(52.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = Mint500, contentColor = Navy900)
                        ) {
                            Icon(Icons.Filled.Person, null, modifier = Modifier.size(18.dp))
                            Spacer(Modifier.width(6.dp))
                            Text("Arrived", fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }

            Slot(3) {
                Text("Other responders", style = MaterialTheme.typography.titleMedium, color = Slate100, fontWeight = FontWeight.SemiBold)
            }

            Slot(4) {
                demoResponders.forEachIndexed { i, r -> ResponderCard(r, animIndex = i) }
            }
        }
    }
}