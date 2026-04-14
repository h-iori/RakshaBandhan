package com.rakshabandhan.sos.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.scaleIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.rakshabandhan.sos.ui.components.DemoFrame
import com.rakshabandhan.sos.ui.components.SecondaryActionButton
import com.rakshabandhan.sos.ui.theme.Coral500
import com.rakshabandhan.sos.ui.theme.Mint500
import com.rakshabandhan.sos.ui.theme.Navy900
import com.rakshabandhan.sos.ui.theme.Slate100
import com.rakshabandhan.sos.ui.theme.Slate200
import kotlinx.coroutines.delay

@Composable
fun AuthScreen(onAuthenticated: () -> Unit = {}) {
    var email by remember { mutableStateOf("priya.sharma@example.com") }
    var otp by remember { mutableStateOf("") }
    var otpSent by remember { mutableStateOf(false) }
    var otpError by remember { mutableStateOf(false) }

    // Staggered entrance
    var headerVisible by remember { mutableStateOf(false) }
    var formVisible by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) {
        delay(80); headerVisible = true
        delay(200); formVisible = true
    }

    DemoFrame(
        title = "Sign in",
        subtitle = "Email OTP keeps onboarding simple.",
        trailing = { Icon(Icons.Filled.Security, null, tint = Slate200) }
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {

            // Brand header
            AnimatedVisibility(
                visible = headerVisible,
                enter = scaleIn(spring(Spring.DampingRatioMediumBouncy)) + fadeIn(tween(300))
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()) {
                    Surface(shape = CircleShape, color = Coral500.copy(alpha = 0.14f), modifier = Modifier.size(64.dp)) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(Icons.Filled.Shield, null, tint = Coral500, modifier = Modifier.size(34.dp))
                        }
                    }
                    Spacer(Modifier.height(10.dp))
                    Text("RakshaBandhan", style = MaterialTheme.typography.titleLarge, color = Slate100, fontWeight = FontWeight.Bold)
                    Text("Your safety network", style = MaterialTheme.typography.bodyMedium, color = Slate200, textAlign = TextAlign.Center)
                }
            }

            Spacer(Modifier.height(4.dp))

            AnimatedVisibility(
                visible = formVisible,
                enter = slideInVertically(initialOffsetY = { it / 2 }, animationSpec = spring(Spring.DampingRatioMediumBouncy)) + fadeIn()
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
                    OutlinedTextField(
                        value = email,
                        onValueChange = { email = it },
                        modifier = Modifier.fillMaxWidth(),
                        label = { Text("Email address") },
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                        leadingIcon = { Icon(Icons.Filled.Email, null) },
                        enabled = !otpSent
                    )

                    if (!otpSent) {
                        Button(onClick = { otpSent = true }, modifier = Modifier.fillMaxWidth()) {
                            Text("Send OTP")
                        }
                        SecondaryActionButton(
                            label = "Continue with Google",
                            onClick = onAuthenticated,
                            modifier = Modifier.fillMaxWidth()
                        )
                    } else {
                        Text(
                            "OTP sent to $email  ·  Use 482913 for demo",
                            style = MaterialTheme.typography.bodySmall,
                            color = Mint500
                        )
                        OutlinedTextField(
                            value = otp,
                            onValueChange = {
                                if (it.length <= 6) { otp = it; otpError = false }
                            },
                            modifier = Modifier.fillMaxWidth(),
                            label = { Text("6-digit OTP") },
                            singleLine = true,
                            isError = otpError,
                            supportingText = if (otpError) {
                                { Text("Incorrect OTP. Try 482913") }
                            } else null,
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword)
                        )
                        Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                            SecondaryActionButton(
                                label = "Resend",
                                onClick = { otp = ""; otpError = false },
                                modifier = Modifier.weight(1f)
                            )
                            Button(
                                onClick = {
                                    if (otp == "482913") onAuthenticated()
                                    else otpError = true
                                },
                                modifier = Modifier.weight(1f),
                                colors = ButtonDefaults.buttonColors(containerColor = Coral500, contentColor = Navy900)
                            ) { Text("Verify OTP") }
                        }
                    }

                    Text(
                        "Static demo — no real backend calls.",
                        style = MaterialTheme.typography.bodySmall,
                        color = Slate200.copy(alpha = 0.6f)
                    )
                }
            }
        }
    }
}