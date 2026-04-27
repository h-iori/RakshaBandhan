package com.rakshabandhan.sos.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.BorderStroke
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
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.rakshabandhan.sos.ui.components.DemoFrame
import com.rakshabandhan.sos.ui.haptics.AppHapticEvent
import com.rakshabandhan.sos.ui.haptics.withHaptic
import com.rakshabandhan.sos.ui.haptics.hapticClickable
import com.rakshabandhan.sos.ui.theme.Coral500
import com.rakshabandhan.sos.ui.theme.Mint500
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

private val profileGenderOptions = listOf("Male", "Female", "Other", "Prefer not to say")

@Composable
fun ProfileSettingsScreen(onBack: () -> Unit) {
    // Pre-filled with demo user data
    var name by remember { mutableStateOf("Priya Sharma") }
    var selectedGender by remember { mutableStateOf("Female") }
    var nameError by remember { mutableStateOf(false) }
    var saved by remember { mutableStateOf(false) }

    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    // Entrance animation
    var visible by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) { delay(80); visible = true }

    Box {
        DemoFrame(
            title = "Profile Settings",
            subtitle = "Update your personal information.",
            trailing = {
                IconButton(onClick = withHaptic(AppHapticEvent.TAP, onBack)) {
                    Icon(Icons.Filled.ArrowBack, contentDescription = "Back", tint = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }
        ) {
            AnimatedVisibility(
                visible = visible,
                enter = slideInVertically(
                    initialOffsetY = { it / 3 },
                    animationSpec = spring(Spring.DampingRatioMediumBouncy, Spring.StiffnessMediumLow)
                ) + fadeIn(tween(300))
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(20.dp)) {

                    // Avatar section
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Surface(
                            shape = CircleShape,
                            color = Coral500.copy(alpha = 0.16f),
                            border = BorderStroke(2.dp, Coral500.copy(alpha = 0.4f)),
                            modifier = Modifier.size(80.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Text(
                                    text = name.split(" ")
                                        .filter { it.isNotBlank() }
                                        .take(2)
                                        .joinToString("") { it.first().uppercaseChar().toString() }
                                        .ifEmpty { "PS" },
                                    style = MaterialTheme.typography.headlineMedium,
                                    color = Coral500,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                        Spacer(Modifier.height(8.dp))
                        Text(
                            "Your profile avatar",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                    // Name field
                    OutlinedTextField(
                        value = name,
                        onValueChange = {
                            name = it
                            nameError = false
                            saved = false
                        },
                        modifier = Modifier.fillMaxWidth(),
                        label = { Text("Full Name *") },
                        singleLine = true,
                        isError = nameError,
                        supportingText = if (nameError) {
                            { Text("Name cannot be empty") }
                        } else null,
                        leadingIcon = { Icon(Icons.Filled.Person, null, tint = MaterialTheme.colorScheme.onSurfaceVariant) },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text)
                    )

                    // Gender selection
                    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        Text(
                            "Gender",
                            style = MaterialTheme.typography.labelLarge,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            profileGenderOptions.take(2).forEach { gender ->
                                ProfileGenderChip(
                                    label = gender,
                                    isSelected = selectedGender == gender,
                                    onSelect = { selectedGender = gender; saved = false },
                                    modifier = Modifier.weight(1f)
                                )
                            }
                        }
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            profileGenderOptions.drop(2).forEach { gender ->
                                ProfileGenderChip(
                                    label = gender,
                                    isSelected = selectedGender == gender,
                                    onSelect = { selectedGender = gender; saved = false },
                                    modifier = Modifier.weight(1f)
                                )
                            }
                        }
                    }

                    // Info row
                    Surface(
                        shape = MaterialTheme.shapes.medium,
                        color = MaterialTheme.colorScheme.surface,
                        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.4f))
                    ) {
                        Column(
                            modifier = Modifier.padding(14.dp),
                            verticalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            LabelValue("Email", "priya.sharma@example.com")
                            LabelValue("Phone", "+91 98765 43210")
                            LabelValue("Member since", "January 2024")
                        }
                    }

                    // Save button
                    Button(
                        onClick = withHaptic(AppHapticEvent.TAP) {
                            nameError = name.isBlank()
                            if (!nameError) {
                                saved = true
                                scope.launch {
                                    snackbarHostState.showSnackbar("Profile updated successfully!")
                                }
                            }
                        },
                        modifier = Modifier.fillMaxWidth().height(52.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (saved) Mint500 else Coral500,
                            contentColor = MaterialTheme.colorScheme.onPrimary
                        )
                    ) {
                        if (saved) {
                            Icon(
                                Icons.Filled.Check,
                                contentDescription = null,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(Modifier.size(6.dp))
                            Text("Saved!", fontWeight = FontWeight.Bold)
                        } else {
                            Text("Save Changes", fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }

        SnackbarHost(
            hostState = snackbarHostState,
            modifier = Modifier.align(Alignment.BottomCenter)
        )
    }
}

@Composable
private fun LabelValue(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(label, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
        Text(value, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurface, fontWeight = FontWeight.Medium)
    }
}

@Composable
private fun ProfileGenderChip(
    label: String,
    isSelected: Boolean,
    onSelect: () -> Unit,
    modifier: Modifier = Modifier
) {
    FilterChip(
        selected = isSelected,
        onClick = withHaptic(AppHapticEvent.SELECTION, onSelect),
        label = { Text(label, style = MaterialTheme.typography.labelMedium, maxLines = 1) },
        modifier = modifier,
        colors = FilterChipDefaults.filterChipColors(
            selectedContainerColor = Coral500.copy(alpha = 0.18f),
            selectedLabelColor = Coral500,
            containerColor = MaterialTheme.colorScheme.surfaceVariant,
            labelColor = MaterialTheme.colorScheme.onSurfaceVariant
        ),
        border = FilterChipDefaults.filterChipBorder(
            enabled = true,
            selected = isSelected,
            selectedBorderColor = Coral500.copy(alpha = 0.5f),
            borderColor = MaterialTheme.colorScheme.outline
        )
    )
}