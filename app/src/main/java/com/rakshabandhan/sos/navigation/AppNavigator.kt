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
import androidx.compose.foundation.interaction.MutableInteractionSource
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
import androidx.compose.foundation.layout.offset
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
    val drawerItems = remember {
        listOf(
            DrawerItem(DemoScreen.PROFILE_SETTINGS, "Profile Settings", Icons.Filled.Person, Sky500, "Account"),
            DrawerItem(DemoScreen.PRIVACY_POLICY, "Privacy Policy", Icons.Filled.Policy, Coral500, "Legal"),
            DrawerItem(DemoScreen.TERMS_CONDITIONS, "Terms & Conditions", Icons.Filled.Gavel, Coral500, "Legal"),
            DrawerItem(DemoScreen.ABOUT, "About", Icons.Filled.Info, Mint500, "App")
        )
    }
    val sections = remember(drawerItems) { drawerItems.map { it.section }.distinct() }

    // Enterprise Theme Surface
    Surface(
        modifier = Modifier
            .width(320.dp) // Slightly wider for enterprise data
            .fillMaxHeight(),
        color = MaterialTheme.colorScheme.background // Solid, clean background
    ) {
        Column(modifier = Modifier.fillMaxSize()) {

            // ── Top App Brand Anchor ──────────────────────────────────────────
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .statusBarsPadding()
                    .padding(horizontal = 24.dp, vertical = 20.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .background(Coral500.copy(alpha = 0.15f), RoundedCornerShape(8.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        Icons.Filled.Shield,
                        contentDescription = null,
                        tint = Coral500,
                        modifier = Modifier.size(18.dp)
                    )
                }
                Text(
                    "RakshaBandhan SOS",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onBackground,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 0.5.sp
                )
            }

            // Divider
            HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)

            // ── Profile Section ───────────────────────────────────────────────
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp, vertical = 24.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Clean Avatar
                Box(
                    modifier = Modifier
                        .size(56.dp)
                        .background(Navy900, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        "PS",
                        style = MaterialTheme.typography.titleMedium,
                        color = Sky500, // Subdued accent instead of bright gradient
                        fontWeight = FontWeight.Bold
                    )
                }

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        "Priya Sharma",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onBackground,
                        fontWeight = FontWeight.SemiBold
                    )
                    Text(
                        "priya.sharma@example.com",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(Modifier.height(4.dp))
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
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
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }

            HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)

            // ── Menu Navigation ───────────────────────────────────────────────
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(top = 8.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                sections.forEach { section ->
                    val sectionItems = drawerItems.filter { it.section == section }

                    Text(
                        text = section.uppercase(),
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        fontWeight = FontWeight.SemiBold,
                        letterSpacing = 1.2.sp,
                        modifier = Modifier.padding(
                            start = 24.dp, end = 24.dp,
                            top = 24.dp, bottom = 8.dp
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
                Spacer(Modifier.height(24.dp))
            }

            // ── Bottom: Actions & Version ─────────────────────────────────────
            ThemeToggleRow()
            
            HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp, vertical = 12.dp)
            ) {
                // Clean text-based logout
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(8.dp))
                        .hapticClickable(hapticEvent = AppHapticEvent.TAP) { onLogout() }
                        .padding(horizontal = 12.dp, vertical = 16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Icon(
                        Icons.Filled.Logout,
                        contentDescription = "Log Out",
                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.size(24.dp)
                    )
                    Text(
                        "Log Out",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onBackground,
                        fontWeight = FontWeight.Medium
                    )
                }

                Text(
                    "Version 1.1.0 (Build 24)",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f),
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .padding(top = 8.dp, bottom = 12.dp)
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
    // Enterprise state transitions are fast and purposeful
    val bgColor by animateColorAsState(
        targetValue = if (isSelected) item.accentColor.copy(alpha = 0.12f) else Color.Transparent,
        animationSpec = tween(150),
        label = "bgColor"
    )
    val contentColor by animateColorAsState(
        targetValue = if (isSelected) item.accentColor else MaterialTheme.colorScheme.onSurfaceVariant,
        animationSpec = tween(150),
        label = "contentColor"
    )

    Box(
        modifier = Modifier
            .padding(horizontal = 12.dp, vertical = 2.dp)
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp)) // Sharper corners for enterprise feel
            .background(bgColor)
            .hapticClickable(hapticEvent = AppHapticEvent.TAP, onClick = onClick)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Icon(
                imageVector = item.icon,
                contentDescription = null,
                tint = contentColor,
                modifier = Modifier.size(24.dp)
            )

            Text(
                text = item.label,
                style = MaterialTheme.typography.bodyLarge,
                color = if (isSelected) MaterialTheme.colorScheme.onBackground else contentColor,
                fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Medium,
                modifier = Modifier.weight(1f)
            )

            if (isSelected) {
                Icon(
                    Icons.Filled.ChevronRight,
                    contentDescription = null,
                    tint = item.accentColor.copy(alpha = 0.5f),
                    modifier = Modifier.size(20.dp)
                )
            }
        }

        // Left accent bar
        if (isSelected) {
            Box(
                modifier = Modifier
                    .align(Alignment.CenterStart)
                    .width(4.dp)
                    .height(24.dp)
                    .clip(RoundedCornerShape(topEnd = 4.dp, bottomEnd = 4.dp))
                    .background(item.accentColor)
            )
        }
    }
}

@Composable
private fun ThemeToggleRow() {
    val themeMode = LocalThemeMode.current
    val isDark = themeMode.isDarkTheme

    val thumbOffset by animateDpAsState(targetValue = if (isDark) 40.dp else 4.dp, animationSpec = spring(dampingRatio = 0.65f, stiffness = 400f), label = "thumbOffset")
    val trackColor by animateColorAsState(targetValue = if (isDark) Color(0xFF1E293B) else Color(0xFFE2E8F0), label = "trackColor")
    val thumbColor by animateColorAsState(targetValue = if (isDark) Color(0xFF334155) else Color.White, label = "thumbColor")
    val iconColor by animateColorAsState(targetValue = if (isDark) Color(0xFFFBBF24) else Color(0xFFF59E0B), label = "iconColor")

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 8.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(Color.Transparent)
            .hapticClickable(hapticEvent = AppHapticEvent.SELECTION) { themeMode.toggleTheme() }
            .padding(horizontal = 16.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            "Appearance",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurface,
            fontWeight = FontWeight.Medium
        )
        
        // Premium Pill Switch
        Box(
            modifier = Modifier
                .width(76.dp)
                .height(40.dp)
                .clip(RoundedCornerShape(20.dp))
                .background(trackColor)
        ) {
            Box(
                modifier = Modifier
                    .offset(x = thumbOffset, y = 4.dp)
                    .size(32.dp)
                    .shadow(elevation = if (isDark) 0.dp else 4.dp, shape = CircleShape)
                    .clip(CircleShape)
                    .background(thumbColor),
                contentAlignment = Alignment.Center
            ) {
                androidx.compose.animation.AnimatedContent(
                    targetState = isDark,
                    transitionSpec = {
                        (fadeIn(tween(300)) + scaleIn(initialScale = 0.5f)) togetherWith (fadeOut(tween(300)) + scaleOut(targetScale = 0.5f))
                    },
                    label = "iconTransition"
                ) { dark ->
                    if (dark) {
                        Icon(Icons.Filled.Nightlight, null, tint = iconColor, modifier = Modifier.size(18.dp))
                    } else {
                        Icon(Icons.Filled.WbSunny, null, tint = iconColor, modifier = Modifier.size(18.dp))
                    }
                }
            }
        }
    }
}
