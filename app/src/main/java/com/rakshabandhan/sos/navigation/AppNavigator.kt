package com.rakshabandhan.sos.navigation

import androidx.compose.material3.HorizontalDivider
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import com.rakshabandhan.sos.ui.haptics.AppHapticEvent
import com.rakshabandhan.sos.ui.haptics.withHaptic
import com.rakshabandhan.sos.ui.haptics.hapticClickable

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Gavel
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.NotificationsActive
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Policy
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Sos
import androidx.compose.material.icons.filled.WbSunny
import androidx.compose.material.icons.filled.Nightlight
import com.rakshabandhan.sos.ui.theme.LocalThemeMode
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.rakshabandhan.sos.model.DemoScreen
import com.rakshabandhan.sos.model.SosState
import com.rakshabandhan.sos.model.demoIncomingAlerts
import com.rakshabandhan.sos.model.demoIncidents
import com.rakshabandhan.sos.ui.components.AppBackground
import com.rakshabandhan.sos.ui.screens.AboutScreen
import com.rakshabandhan.sos.ui.screens.ActiveSosScreen
import com.rakshabandhan.sos.ui.screens.AuthScreen
import com.rakshabandhan.sos.ui.screens.HistoryScreen
import com.rakshabandhan.sos.ui.screens.HomeScreen
import com.rakshabandhan.sos.ui.screens.PrivacyPolicyScreen
import com.rakshabandhan.sos.ui.screens.ProfileSettingsScreen
import com.rakshabandhan.sos.ui.screens.ResponderAlertScreen
import com.rakshabandhan.sos.ui.screens.TermsConditionsScreen
import com.rakshabandhan.sos.ui.theme.Amber500
import com.rakshabandhan.sos.ui.theme.Coral500
import com.rakshabandhan.sos.ui.theme.Mint500
import com.rakshabandhan.sos.ui.theme.Navy900
import com.rakshabandhan.sos.ui.theme.Navy800
import com.rakshabandhan.sos.ui.theme.Navy950
import com.rakshabandhan.sos.ui.theme.Sky500
import com.rakshabandhan.sos.ui.theme.Slate100
import com.rakshabandhan.sos.ui.theme.Slate200
import com.rakshabandhan.sos.ui.theme.Slate700
import com.rakshabandhan.sos.R
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

// ─────────────────────────────────────────────────────────────────────────────

private val screenOrder = listOf(
    DemoScreen.AUTH,
    DemoScreen.HOME,
    DemoScreen.ACTIVE_SOS,
    DemoScreen.RESPONDER_ALERT,
    DemoScreen.HISTORY,
    DemoScreen.PROFILE_SETTINGS,
    DemoScreen.PRIVACY_POLICY,
    DemoScreen.TERMS_CONDITIONS,
    DemoScreen.ABOUT
)

private val bottomNavScreens = setOf(
    DemoScreen.AUTH,
    DemoScreen.HOME,
    DemoScreen.ACTIVE_SOS,
    DemoScreen.RESPONDER_ALERT,
    DemoScreen.HISTORY
)

data class DemoNavItem(
    val screen: DemoScreen,
    val label: String,
    val icon: @Composable () -> Unit
)

// ── DemoApp ───────────────────────────────────────────────────────────────────

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DemoApp() {
    var selected by rememberSaveable { mutableStateOf(DemoScreen.AUTH) }
    var previousScreen by remember { mutableStateOf(DemoScreen.AUTH) }
    val nearbyAlertCount = remember {
        demoIncomingAlerts.count { it.state != SosState.STOPPED }
    }

    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    val snackbarHostState = remember { SnackbarHostState() }
    var backPressedOnHome by remember { mutableStateOf(false) }

    // Auto-reset double-back flag after 2 s
    LaunchedEffect(backPressedOnHome) {
        if (backPressedOnHome) {
            delay(2000)
            backPressedOnHome = false
        }
    }

    // Back-press interception for everything except the AUTH screen
    BackHandler(enabled = selected != DemoScreen.AUTH) {
        when {
            drawerState.isOpen -> {
                scope.launch { drawerState.close() }
            }
            selected != DemoScreen.HOME -> {
                previousScreen = selected
                selected = DemoScreen.HOME
                backPressedOnHome = false
            }
            !backPressedOnHome -> {
                backPressedOnHome = true
                scope.launch {
                    snackbarHostState.showSnackbar("Press back again to exit")
                }
            }
            else -> {
                (context as? androidx.activity.ComponentActivity)?.finish()
            }
        }
    }

    val navItems = remember(nearbyAlertCount) {
        listOf(
            DemoNavItem(DemoScreen.AUTH, "Auth") { Icon(Icons.Filled.AccountCircle, null) },
            DemoNavItem(DemoScreen.HOME, "Home") { Icon(Icons.Filled.Home, null) },
            DemoNavItem(DemoScreen.ACTIVE_SOS, "SOS") { Icon(Icons.Filled.Sos, null) },
            DemoNavItem(DemoScreen.RESPONDER_ALERT, "Alert") { AlertNavIcon(nearbyAlertCount) },
            DemoNavItem(DemoScreen.HISTORY, "History") { Icon(Icons.Filled.History, null) }
        )
    }

    // RTL wrapper makes ModalNavigationDrawer slide from the RIGHT
    CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
        ModalNavigationDrawer(
            drawerState = drawerState,
            gesturesEnabled = true,
            scrimColor = Color.Black.copy(alpha = 0.60f),
            drawerContent = {
                // Flip content back to LTR so text/icons render correctly
                CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Ltr) {
                    ModernDrawerContent(
                        currentScreen = selected,
                        onNavigate = { screen ->
                            scope.launch { drawerState.close() }
                            previousScreen = selected
                            selected = screen
                        },
                        onLogout = {
                            scope.launch { drawerState.close() }
                            previousScreen = selected
                            selected = DemoScreen.AUTH
                        }
                    )
                }
            }
        ) {
            // App scaffold — flip back to LTR
            CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Ltr) {
                AppBackground {
                    Scaffold(
                        containerColor = Color.Transparent,
                        contentWindowInsets = WindowInsets.safeDrawing,
                        snackbarHost = { SnackbarHost(snackbarHostState) },
                        topBar = {
                            // ── Fixed Navbar Logo Implementation ───────
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    // Apply surface background color here so it blends with status bars.
                                    .background(MaterialTheme.colorScheme.surface.copy(alpha = 0.97f))
                                    .statusBarsPadding()
                            ) {
                                TopAppBar(
                                    title = {
                                        // 1. The Image is now inside the title area, meaning it will naturally
                                        // stop where the hamburger menu begins.
                                        androidx.compose.foundation.Image(
                                            painter = androidx.compose.ui.res.painterResource(id = R.drawable.rakshabandhan_navbar),
                                            contentDescription = "RakshaBandhan SOS Logo",
                                            modifier = Modifier
                                                .fillMaxWidth() // Fill the available space up to the hamburger icon
                                                .height(44.dp) // Shrunk slightly from 64dp for a more professional, breathable look
                                                .padding(end = 16.dp), // Gives a little breathing room before the menu icon
                                            // 2. 'Fit' ensures the image scales down proportionally without stretching
                                            contentScale = androidx.compose.ui.layout.ContentScale.Fit,
                                            // 3. Aligns the image vertically to the center and horizontally to the left
                                            alignment = Alignment.CenterStart 
                                        )
                                    },
                                    // ── Hamburger on the RIGHT ──────────────
                                    actions = {
                                        IconButton(
                                            onClick = withHaptic(AppHapticEvent.TAP) { scope.launch { drawerState.open() } }
                                        ) {
                                            Icon(
                                                Icons.Filled.Menu,
                                                contentDescription = "Open menu",
                                                tint = MaterialTheme.colorScheme.onSurface
                                            )
                                        }
                                    },
                                    colors = TopAppBarDefaults.topAppBarColors(
                                        containerColor = Color.Transparent, 
                                        titleContentColor = MaterialTheme.colorScheme.onSurface
                                    )
                                )
                            }
                        },
                        bottomBar = {
                            if (selected in bottomNavScreens) {
                                NavigationBar(containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.97f)) {
                                    navItems.forEach { item ->
                                        NavigationBarItem(
                                            selected = selected == item.screen,
                                            onClick = withHaptic(
                                                if (item.screen == DemoScreen.ACTIVE_SOS) AppHapticEvent.CONFIRM else AppHapticEvent.TAP
                                            ) {
                                                previousScreen = selected
                                                selected = item.screen
                                            },
                                            icon = item.icon,
                                            label = {
                                                Text(
                                                    item.label,
                                                    style = MaterialTheme.typography.labelSmall
                                                )
                                            },
                                            colors = NavigationBarItemDefaults.colors(
                                                selectedIconColor = Coral500,
                                                selectedTextColor = Coral500,
                                                unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                                                unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant,
                                                indicatorColor = Coral500.copy(alpha = 0.14f)
                                            )
                                        )
                                    }
                                }
                            }
                        }
                    ) { padding ->
                        // derivedStateOf prevents recomposition from bubbling unnecessarily
                        val prevIdx by remember(previousScreen) {
                            derivedStateOf {
                                screenOrder.indexOf(previousScreen).coerceAtLeast(0)
                            }
                        }
                        val currIdx by remember(selected) {
                            derivedStateOf {
                                screenOrder.indexOf(selected).coerceAtLeast(0)
                            }
                        }
                        val goingRight by remember(prevIdx, currIdx) {
                            derivedStateOf { currIdx >= prevIdx }
                        }

                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(padding)
                                .padding(horizontal = 16.dp, vertical = 12.dp)
                                .verticalScroll(rememberScrollState()),
                            verticalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            // Snappier transitions: use tween instead of spring for the slide
                            AnimatedContent(
                                targetState = selected,
                                transitionSpec = {
                                    (slideInHorizontally(
                                        initialOffsetX = { if (goingRight) it else -it },
                                        animationSpec = tween(260, easing = FastOutSlowInEasing)
                                    ) + fadeIn(tween(200))) togetherWith
                                            (slideOutHorizontally(
                                                targetOffsetX = { if (goingRight) -it / 4 else it / 4 },
                                                animationSpec = tween(220, easing = FastOutSlowInEasing)
                                            ) + fadeOut(tween(160)))
                                },
                                label = "screen"
                            ) { screen ->
                                when (screen) {
                                    DemoScreen.AUTH -> AuthScreen(
                                        onAuthenticated = {
                                            previousScreen = selected
                                            selected = DemoScreen.HOME
                                        }
                                    )
                                    DemoScreen.HOME -> HomeScreen(
                                        onGoToSos = {
                                            previousScreen = selected
                                            selected = DemoScreen.ACTIVE_SOS
                                        }
                                    )
                                    DemoScreen.ACTIVE_SOS -> ActiveSosScreen(
                                        onStop = {
                                            previousScreen = selected
                                            selected = DemoScreen.HOME
                                        },
                                        onExtend = {}
                                    )
                                    DemoScreen.RESPONDER_ALERT -> ResponderAlertScreen()
                                    DemoScreen.HISTORY -> HistoryScreen(incidents = demoIncidents)
                                    DemoScreen.PROFILE_SETTINGS -> ProfileSettingsScreen(
                                        onBack = {
                                            previousScreen = selected
                                            selected = DemoScreen.HOME
                                        }
                                    )
                                    DemoScreen.PRIVACY_POLICY -> PrivacyPolicyScreen(
                                        onBack = {
                                            previousScreen = selected
                                            selected = DemoScreen.HOME
                                        }
                                    )
                                    DemoScreen.TERMS_CONDITIONS -> TermsConditionsScreen(
                                        onBack = {
                                            previousScreen = selected
                                            selected = DemoScreen.HOME
                                        }
                                    )
                                    DemoScreen.ABOUT -> AboutScreen(
                                        onBack = {
                                            previousScreen = selected
                                            selected = DemoScreen.HOME
                                        }
                                    )
                                }
                            }
                            Spacer(Modifier.height(8.dp))
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun AlertNavIcon(count: Int) {
    if (count > 0) {
        BadgedBox(
            badge = {
                Badge(
                    containerColor = Coral500,
                    contentColor = Navy950
                ) {
                    Text(
                        text = count.coerceAtMost(99).toString(),
                        style = MaterialTheme.typography.labelSmall
                    )
                }
            }
        ) {
            Icon(Icons.Filled.NotificationsActive, null)
        }
    } else {
        Icon(Icons.Filled.NotificationsActive, null)
    }
}

// ── Modern Drawer Content ─────────────────────────────────────────────────────

private data class DrawerItem(
    val screen: DemoScreen,
    val label: String,
    val icon: ImageVector,
    val accentColor: Color,
    val section: String
)

@Composable
fun ModernDrawerContent(
    currentScreen: DemoScreen,
    onNavigate: (DemoScreen) -> Unit,
    onLogout: () -> Unit
) {
    val themeMode = LocalThemeMode.current
    val isDark = themeMode.isDarkTheme

    val drawerItems = remember {
        listOf(
            DrawerItem(DemoScreen.PROFILE_SETTINGS, "Profile Settings", Icons.Filled.Person, Sky500, "Account"),
            DrawerItem(DemoScreen.PRIVACY_POLICY, "Privacy Policy", Icons.Filled.Policy, Coral500, "Legal"),
            DrawerItem(DemoScreen.TERMS_CONDITIONS, "Terms & Conditions", Icons.Filled.Gavel, Coral500, "Legal"),
            DrawerItem(DemoScreen.ABOUT, "About", Icons.Filled.Info, Mint500, "App")
        )
    }
    val sections = remember(drawerItems) { drawerItems.map { it.section }.distinct() }

    Surface(
        modifier = Modifier
            .width(310.dp)
            .fillMaxHeight(),
        color = MaterialTheme.colorScheme.background,
        shadowElevation = 16.dp
    ) {
        Column(modifier = Modifier.fillMaxSize()) {

            // ── Brand Header with subtle gradient accent ─────────────────────
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        Brush.verticalGradient(
                            listOf(
                                Coral500.copy(alpha = if (isDark) 0.08f else 0.05f),
                                Color.Transparent
                            )
                        )
                    )
                    .statusBarsPadding()
                    .padding(horizontal = 20.dp, vertical = 18.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .background(
                                    Brush.linearGradient(listOf(Coral500.copy(alpha = 0.2f), Coral500.copy(alpha = 0.08f))),
                                    RoundedCornerShape(10.dp)
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                Icons.Filled.Shield,
                                contentDescription = null,
                                tint = Coral500,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                        Column {
                            Text(
                                "RakshaBandhan",
                                style = MaterialTheme.typography.titleMedium,
                                color = MaterialTheme.colorScheme.onBackground,
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 0.3.sp
                            )
                            Text(
                                "SOS Platform",
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }

                }
            }

            HorizontalDivider(
                color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.6f),
                thickness = 0.5.dp
            )

            // ── Profile Card ─────────────────────────────────────────────────
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 20.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                // Avatar with gradient ring
                Box(contentAlignment = Alignment.Center) {
                    Box(
                        modifier = Modifier
                            .size(52.dp)
                            .background(
                                Brush.linearGradient(listOf(Sky500.copy(alpha = 0.3f), Coral500.copy(alpha = 0.2f))),
                                CircleShape
                            )
                    )
                    Box(
                        modifier = Modifier
                            .size(48.dp)
                            .background(
                                if (isDark) Navy900 else MaterialTheme.colorScheme.surfaceVariant,
                                CircleShape
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            "PS",
                            style = MaterialTheme.typography.titleSmall,
                            color = if (isDark) Sky500 else Sky500,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        "Priya Sharma",
                        style = MaterialTheme.typography.titleSmall,
                        color = MaterialTheme.colorScheme.onBackground,
                        fontWeight = FontWeight.SemiBold
                    )
                    Text(
                        "priya.sharma@example.com",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        maxLines = 1
                    )
                    Spacer(Modifier.height(4.dp))
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(5.dp)
                    ) {
                        Box(
                            Modifier
                                .size(6.dp)
                                .background(Mint500, CircleShape)
                        )
                        Text(
                            "Active Session",
                            style = MaterialTheme.typography.labelSmall,
                            color = Mint500,
                            fontWeight = FontWeight.Medium,
                            fontSize = 10.sp
                        )
                    }
                }
            }

            HorizontalDivider(
                color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.6f),
                thickness = 0.5.dp
            )

            // ── Menu Navigation ──────────────────────────────────────────────
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(top = 4.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                sections.forEach { section ->
                    val sectionItems = drawerItems.filter { it.section == section }

                    Text(
                        text = section.uppercase(),
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f),
                        fontWeight = FontWeight.SemiBold,
                        letterSpacing = 1.4.sp,
                        fontSize = 10.sp,
                        modifier = Modifier.padding(
                            start = 24.dp, end = 24.dp,
                            top = 20.dp, bottom = 6.dp
                        )
                    )

                    sectionItems.forEach { item ->
                        ModernDrawerItem(
                            item = item,
                            isSelected = currentScreen == item.screen,
                            onClick = { onNavigate(item.screen) }
                        )
                    }
                }
                Spacer(Modifier.height(16.dp))
            }

            // ── Neon Theme Toggle Pill ────────────────────────────────────────
            NeonThemeTogglePill(
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp)
            )

            // ── Bottom: Logout + Version (pinned) ────────────────────────────
            HorizontalDivider(
                color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.6f),
                thickness = 0.5.dp
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp, vertical = 8.dp)
            ) {
                // Red Logout Button
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color(0xFFDC2626).copy(alpha = if (isDark) 0.12f else 0.08f))
                        .hapticClickable(hapticEvent = AppHapticEvent.TAP) { onLogout() }
                        .padding(horizontal = 16.dp, vertical = 14.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    Icon(
                        Icons.Filled.Logout,
                        contentDescription = "Log Out",
                        tint = Color(0xFFDC2626),
                        modifier = Modifier.size(22.dp)
                    )
                    Text(
                        "Log Out",
                        style = MaterialTheme.typography.bodyLarge,
                        color = Color(0xFFDC2626),
                        fontWeight = FontWeight.SemiBold
                    )
                }

                Text(
                    "Version 1.1.0 (Build 24)",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.45f),
                    fontSize = 10.sp,
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .padding(top = 10.dp, bottom = 8.dp)
                )

                Spacer(Modifier.navigationBarsPadding())
            }
        }
    }
}

// ── Individual drawer row ─────────────────────────────────────────────────────

@Composable
private fun ModernDrawerItem(
    item: DrawerItem,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val bgColor by animateColorAsState(
        targetValue = if (isSelected) item.accentColor.copy(alpha = 0.10f) else Color.Transparent,
        animationSpec = tween(180),
        label = "bgColor"
    )
    val contentColor by animateColorAsState(
        targetValue = if (isSelected) item.accentColor else MaterialTheme.colorScheme.onSurfaceVariant,
        animationSpec = tween(180),
        label = "contentColor"
    )

    Box(
        modifier = Modifier
            .padding(horizontal = 12.dp, vertical = 2.dp)
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(bgColor)
            .hapticClickable(hapticEvent = AppHapticEvent.TAP, onClick = onClick)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 14.dp, vertical = 13.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // Icon container
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .background(
                        if (isSelected) item.accentColor.copy(alpha = 0.12f) else Color.Transparent,
                        RoundedCornerShape(10.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = item.icon,
                    contentDescription = null,
                    tint = contentColor,
                    modifier = Modifier.size(20.dp)
                )
            }

            Text(
                text = item.label,
                style = MaterialTheme.typography.bodyMedium,
                color = if (isSelected) MaterialTheme.colorScheme.onBackground else contentColor,
                fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Medium,
                modifier = Modifier.weight(1f)
            )

            if (isSelected) {
                Icon(
                    Icons.Filled.ChevronRight,
                    contentDescription = null,
                    tint = item.accentColor.copy(alpha = 0.5f),
                    modifier = Modifier.size(18.dp)
                )
            }
        }

        // Left accent bar
        if (isSelected) {
            Box(
                modifier = Modifier
                    .align(Alignment.CenterStart)
                    .width(3.dp)
                    .height(22.dp)
                    .clip(RoundedCornerShape(topEnd = 4.dp, bottomEnd = 4.dp))
                    .background(item.accentColor)
            )
        }
    }
}

// ── Neon Theme Toggle Pill (reference-style capsule button) ───────────────────

@Composable
private fun NeonThemeTogglePill(modifier: Modifier = Modifier) {
    val themeMode = LocalThemeMode.current
    val isDark = themeMode.isDarkTheme

    // Animated properties
    val pillGradientStart by animateColorAsState(
        targetValue = if (isDark) Navy950 else Color(0xFFF8FAFC),
        animationSpec = tween(400),
        label = "pillGradStart"
    )
    val pillGradientEnd by animateColorAsState(
        targetValue = if (isDark) Navy800 else Color(0xFFE2E8F0),
        animationSpec = tween(400),
        label = "pillGradEnd"
    )
    val orbBg by animateColorAsState(
        targetValue = if (isDark) Coral500.copy(alpha = 0.15f) else Color(0xFFFEF3C7),
        animationSpec = tween(350),
        label = "orbBg"
    )
    val orbBorder by animateColorAsState(
        targetValue = if (isDark) Coral500.copy(alpha = 0.35f) else Amber500.copy(alpha = 0.4f),
        animationSpec = tween(350),
        label = "orbBorder"
    )
    val textColor by animateColorAsState(
        targetValue = if (isDark) Slate200 else Color(0xFF334155),
        animationSpec = tween(300),
        label = "pillText"
    )
    val iconColor by animateColorAsState(
        targetValue = if (isDark) Color(0xFFFBBF24) else Color(0xFFF59E0B),
        animationSpec = tween(300),
        label = "pillIcon"
    )
    // Glow accent behind the orb
    val glowColor by animateColorAsState(
        targetValue = if (isDark) Coral500.copy(alpha = 0.12f) else Amber500.copy(alpha = 0.10f),
        animationSpec = tween(350),
        label = "glow"
    )

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(56.dp)
            .clip(RoundedCornerShape(28.dp))
            .background(
                Brush.horizontalGradient(listOf(pillGradientStart, pillGradientEnd))
            )
            .hapticClickable(hapticEvent = AppHapticEvent.SELECTION) { themeMode.toggleTheme() },
        contentAlignment = Alignment.CenterStart
    ) {
        // Subtle inner border
        Box(
            modifier = Modifier
                .matchParentSize()
                .clip(RoundedCornerShape(28.dp))
                .background(Color.Transparent)
                .then(
                    Modifier.padding(0.5.dp)
                )
        )

        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(start = 20.dp, end = 6.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // Text label
            AnimatedContent(
                targetState = isDark,
                transitionSpec = {
                    (fadeIn(tween(300)) + slideInHorizontally { -it / 3 }) togetherWith
                            (fadeOut(tween(200)) + slideOutHorizontally { it / 3 })
                },
                label = "modeLabel"
            ) { dark ->
                Text(
                    text = if (dark) "DARK MODE" else "LIGHT MODE",
                    style = MaterialTheme.typography.titleSmall,
                    color = textColor,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 2.sp
                )
            }

            // Large circular orb with icon
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .background(glowColor, CircleShape) // subtle glow ring
                    .padding(2.dp)
                    .clip(CircleShape)
                    .background(orbBg)
                    .then(
                        Modifier
                            .clip(CircleShape)
                            .background(
                                Brush.radialGradient(
                                    listOf(
                                        orbBg,
                                        orbBg.copy(alpha = 0.6f)
                                    )
                                )
                            )
                    ),
                contentAlignment = Alignment.Center
            ) {
                AnimatedContent(
                    targetState = isDark,
                    transitionSpec = {
                        (fadeIn(tween(300)) + scaleIn(
                            initialScale = 0.3f,
                            animationSpec = spring(dampingRatio = 0.5f, stiffness = 400f)
                        )) togetherWith
                                (fadeOut(tween(180)) + scaleOut(targetScale = 0.3f))
                    },
                    label = "orbIcon"
                ) { dark ->
                    Icon(
                        imageVector = if (dark) Icons.Filled.Nightlight else Icons.Filled.WbSunny,
                        contentDescription = if (dark) "Switch to light mode" else "Switch to dark mode",
                        tint = iconColor,
                        modifier = Modifier.size(24.dp)
                    )
                }
            }
        }
    }
}
