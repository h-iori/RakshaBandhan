package com.rakshabandhan.sos.ui.screens

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Badge
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
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
import com.rakshabandhan.sos.ui.theme.Navy800
import com.rakshabandhan.sos.ui.theme.Navy900
import com.rakshabandhan.sos.ui.theme.Slate100
import com.rakshabandhan.sos.ui.theme.Slate200
import com.rakshabandhan.sos.ui.theme.Slate700
import kotlinx.coroutines.delay

// Auth steps for the email sign-up flow
private enum class AuthStep { CREDENTIAL, PROFILE_COMPLETION }

private val genderOptions = listOf("Male", "Female", "Other", "Prefer not to say")

@Composable
fun AuthScreen(onAuthenticated: () -> Unit = {}) {
    // ── Credential step state ────────────────────────────────────────────────
    var email by remember { mutableStateOf("priya.sharma@example.com") }
    var otp by remember { mutableStateOf("") }
    var otpSent by remember { mutableStateOf(false) }
    var otpError by remember { mutableStateOf(false) }

    // ── Profile completion step state ────────────────────────────────────────
    var firstName by remember { mutableStateOf("") }
    var surname by remember { mutableStateOf("") }
    var selectedGender by remember { mutableStateOf("") }
    var firstNameError by remember { mutableStateOf(false) }
    var surnameError by remember { mutableStateOf(false) }
    var genderError by remember { mutableStateOf(false) }

    // ── Which auth step we are on ────────────────────────────────────────────
    var authStep by remember { mutableStateOf(AuthStep.CREDENTIAL) }

    // Staggered entrance animations
    var headerVisible by remember { mutableStateOf(false) }
    var formVisible by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) {
        delay(80); headerVisible = true
        delay(200); formVisible = true
    }

    AnimatedContent(
        targetState = authStep,
        transitionSpec = {
            (slideInHorizontally(
                initialOffsetX = { it },
                animationSpec = spring(Spring.DampingRatioMediumBouncy, Spring.StiffnessMediumLow)
            ) + fadeIn(tween(220))) togetherWith
                    (slideOutHorizontally(
                        targetOffsetX = { -it / 3 },
                        animationSpec = tween(200)
                    ) + fadeOut(tween(160)))
        },
        label = "authStep"
    ) { step ->
        when (step) {
            AuthStep.CREDENTIAL -> CredentialStep(
                email = email,
                onEmailChange = { email = it },
                otp = otp,
                onOtpChange = {
                    if (it.length <= 6) { otp = it; otpError = false }
                },
                otpSent = otpSent,
                otpError = otpError,
                headerVisible = headerVisible,
                formVisible = formVisible,
                onSendOtp = { otpSent = true },
                onVerifyOtp = {
                    if (otp == "482913") {
                        // Email flow → go to profile completion
                        authStep = AuthStep.PROFILE_COMPLETION
                    } else {
                        otpError = true
                    }
                },
                onGoogleLogin = onAuthenticated
            )
            AuthStep.PROFILE_COMPLETION -> ProfileCompletionStep(
                firstName = firstName,
                onFirstNameChange = { firstName = it; firstNameError = false },
                surname = surname,
                onSurnameChange = { surname = it; surnameError = false },
                selectedGender = selectedGender,
                onGenderSelect = { selectedGender = it; genderError = false },
                firstNameError = firstNameError,
                surnameError = surnameError,
                genderError = genderError,
                onContinue = {
                    firstNameError = firstName.isBlank()
                    surnameError = surname.isBlank()
                    genderError = selectedGender.isBlank()
                    if (!firstNameError && !surnameError && !genderError) {
                        onAuthenticated()
                    }
                }
            )
        }
    }
}

// ── Step 1: Email / OTP ───────────────────────────────────────────────────────

@Composable
private fun CredentialStep(
    email: String,
    onEmailChange: (String) -> Unit,
    otp: String,
    onOtpChange: (String) -> Unit,
    otpSent: Boolean,
    otpError: Boolean,
    headerVisible: Boolean,
    formVisible: Boolean,
    onSendOtp: () -> Unit,
    onVerifyOtp: () -> Unit,
    onGoogleLogin: () -> Unit
) {
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
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Surface(
                        shape = CircleShape,
                        color = Coral500.copy(alpha = 0.14f),
                        modifier = Modifier.size(64.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(Icons.Filled.Shield, null, tint = Coral500, modifier = Modifier.size(34.dp))
                        }
                    }
                    Spacer(Modifier.height(10.dp))
                    Text(
                        "RakshaBandhan",
                        style = MaterialTheme.typography.titleLarge,
                        color = Slate100,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        "Your safety network",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Slate200,
                        textAlign = TextAlign.Center
                    )
                }
            }

            Spacer(Modifier.height(4.dp))

            AnimatedVisibility(
                visible = formVisible,
                enter = slideInVertically(
                    initialOffsetY = { it / 2 },
                    animationSpec = spring(Spring.DampingRatioMediumBouncy)
                ) + fadeIn()
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
                    OutlinedTextField(
                        value = email,
                        onValueChange = onEmailChange,
                        modifier = Modifier.fillMaxWidth(),
                        label = { Text("Email address") },
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                        leadingIcon = { Icon(Icons.Filled.Email, null) },
                        enabled = !otpSent
                    )

                    if (!otpSent) {
                        Button(
                            onClick = onSendOtp,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("Send OTP")
                        }
                        SecondaryActionButton(
                            label = "Continue with Google",
                            onClick = onGoogleLogin,
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
                            onValueChange = onOtpChange,
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
                                onClick = { /* reset otp state handled above */ },
                                modifier = Modifier.weight(1f)
                            )
                            Button(
                                onClick = onVerifyOtp,
                                modifier = Modifier.weight(1f),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Coral500,
                                    contentColor = Navy900
                                )
                            ) { Text("Verify OTP") }
                        }
                    }

                    Text(
                        "By continuing, you agree to our Privacy Policy and Terms of Service.",
                        style = MaterialTheme.typography.bodySmall,
                        color = Slate200.copy(alpha = 0.6f)
                    )
                }
            }
        }
    }
}

// ── Step 2: Profile Completion (email-only path) ──────────────────────────────

@Composable
private fun ProfileCompletionStep(
    firstName: String,
    onFirstNameChange: (String) -> Unit,
    surname: String,
    onSurnameChange: (String) -> Unit,
    selectedGender: String,
    onGenderSelect: (String) -> Unit,
    firstNameError: Boolean,
    surnameError: Boolean,
    genderError: Boolean,
    onContinue: () -> Unit
) {
    // Staggered entrance for profile step
    var visible by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) { delay(100); visible = true }

    DemoFrame(
        title = "Complete your profile",
        subtitle = "Just a few more details to get started.",
        trailing = { Icon(Icons.Filled.Badge, null, tint = Coral500) }
    ) {
        AnimatedVisibility(
            visible = visible,
            enter = slideInVertically(
                initialOffsetY = { it / 3 },
                animationSpec = spring(Spring.DampingRatioMediumBouncy, Spring.StiffnessMediumLow)
            ) + fadeIn(tween(300))
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {

                // Step indicator
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = Mint500.copy(alpha = 0.12f),
                    border = BorderStroke(1.dp, Mint500.copy(alpha = 0.3f))
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(
                            Icons.Filled.CheckCircle,
                            contentDescription = null,
                            tint = Mint500,
                            modifier = Modifier.size(16.dp)
                        )
                        Text(
                            "Email verified! Now tell us about yourself.",
                            style = MaterialTheme.typography.bodySmall,
                            color = Mint500
                        )
                    }
                }

                // First name
                OutlinedTextField(
                    value = firstName,
                    onValueChange = onFirstNameChange,
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text("First name *") },
                    singleLine = true,
                    isError = firstNameError,
                    supportingText = if (firstNameError) {
                        { Text("First name is required") }
                    } else null,
                    leadingIcon = { Icon(Icons.Filled.Person, null) },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text)
                )

                // Surname
                OutlinedTextField(
                    value = surname,
                    onValueChange = onSurnameChange,
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text("Surname *") },
                    singleLine = true,
                    isError = surnameError,
                    supportingText = if (surnameError) {
                        { Text("Surname is required") }
                    } else null,
                    leadingIcon = { Icon(Icons.Filled.Person, null) },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text)
                )

                // Gender selection
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(
                        "Gender *",
                        style = MaterialTheme.typography.labelLarge,
                        color = if (genderError) MaterialTheme.colorScheme.error else Slate200
                    )
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        genderOptions.take(2).forEach { gender ->
                            GenderChip(
                                label = gender,
                                isSelected = selectedGender == gender,
                                onSelect = { onGenderSelect(gender) },
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        genderOptions.drop(2).forEach { gender ->
                            GenderChip(
                                label = gender,
                                isSelected = selectedGender == gender,
                                onSelect = { onGenderSelect(gender) },
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }
                    if (genderError) {
                        Text(
                            "Please select your gender",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.error
                        )
                    }
                }

                Spacer(Modifier.height(4.dp))

                Button(
                    onClick = onContinue,
                    modifier = Modifier.fillMaxWidth().height(52.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Coral500,
                        contentColor = Navy900
                    )
                ) {
                    Text(
                        "Continue to App",
                        style = MaterialTheme.typography.labelLarge,
                        fontWeight = FontWeight.Bold
                    )
                }

                Text(
                    "* Required fields",
                    style = MaterialTheme.typography.bodySmall,
                    color = Slate200.copy(alpha = 0.55f)
                )
            }
        }
    }
}

@Composable
private fun GenderChip(
    label: String,
    isSelected: Boolean,
    onSelect: () -> Unit,
    modifier: Modifier = Modifier
) {
    FilterChip(
        selected = isSelected,
        onClick = onSelect,
        label = {
            Text(
                label,
                style = MaterialTheme.typography.labelMedium,
                maxLines = 1
            )
        },
        modifier = modifier,
        colors = FilterChipDefaults.filterChipColors(
            selectedContainerColor = Coral500.copy(alpha = 0.18f),
            selectedLabelColor = Coral500,
            containerColor = Navy800,
            labelColor = Slate200
        ),
        border = FilterChipDefaults.filterChipBorder(
            enabled = true,
            selected = isSelected,
            selectedBorderColor = Coral500.copy(alpha = 0.5f),
            borderColor = Slate700
        )
    )
}