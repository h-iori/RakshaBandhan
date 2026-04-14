package com.rakshabandhan.sos.navigation

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.NotificationsActive
import androidx.compose.material.icons.filled.Sos
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.rakshabandhan.sos.model.DemoScreen
import com.rakshabandhan.sos.model.demoIncidents
import com.rakshabandhan.sos.ui.components.AppBackground
import com.rakshabandhan.sos.ui.screens.ActiveSosScreen
import com.rakshabandhan.sos.ui.screens.AuthScreen
import com.rakshabandhan.sos.ui.screens.HistoryScreen
import com.rakshabandhan.sos.ui.screens.HomeScreen
import com.rakshabandhan.sos.ui.screens.ResponderAlertScreen
import com.rakshabandhan.sos.ui.theme.Coral500
import com.rakshabandhan.sos.ui.theme.Navy900
import com.rakshabandhan.sos.ui.theme.Slate100
import com.rakshabandhan.sos.ui.theme.Slate200

private val screenOrder = listOf(
    DemoScreen.AUTH, DemoScreen.HOME, DemoScreen.ACTIVE_SOS,
    DemoScreen.RESPONDER_ALERT, DemoScreen.HISTORY
)

data class DemoNavItem(
    val screen: DemoScreen,
    val label: String,
    val icon: @Composable () -> Unit
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DemoApp() {
    var selected by rememberSaveable { mutableStateOf(DemoScreen.AUTH) }
    var previousScreen by remember { mutableStateOf(DemoScreen.AUTH) }

    val navItems = remember {
        listOf(
            DemoNavItem(DemoScreen.AUTH, "Auth") { Icon(Icons.Filled.AccountCircle, null) },
            DemoNavItem(DemoScreen.HOME, "Home") { Icon(Icons.Filled.Home, null) },
            DemoNavItem(DemoScreen.ACTIVE_SOS, "SOS") { Icon(Icons.Filled.Sos, null) },
            DemoNavItem(DemoScreen.RESPONDER_ALERT, "Alert") { Icon(Icons.Filled.NotificationsActive, null) },
            DemoNavItem(DemoScreen.HISTORY, "History") { Icon(Icons.Filled.History, null) }
        )
    }

    AppBackground {
        Scaffold(
            containerColor = Color.Transparent,
            contentWindowInsets = WindowInsets.safeDrawing,
            topBar = {
                TopAppBar(
                    title = {
                        Column {
                            Text("RakshaBandhan SOS", color = Slate100, style = MaterialTheme.typography.titleMedium)
                            Text("Demo shell — static data", color = Slate200, style = MaterialTheme.typography.labelSmall)
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = Navy900.copy(alpha = 0.95f),
                        titleContentColor = Slate100
                    )
                )
            },
            bottomBar = {
                NavigationBar(containerColor = Navy900.copy(alpha = 0.97f)) {
                    navItems.forEach { item ->
                        NavigationBarItem(
                            selected = selected == item.screen,
                            onClick = {
                                previousScreen = selected
                                selected = item.screen
                            },
                            icon = item.icon,
                            label = { Text(item.label, style = MaterialTheme.typography.labelSmall) },
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = Coral500,
                                selectedTextColor = Coral500,
                                unselectedIconColor = Slate200,
                                unselectedTextColor = Slate200,
                                indicatorColor = Coral500.copy(alpha = 0.14f)
                            )
                        )
                    }
                }
            }
        ) { padding ->
            val prevIdx = screenOrder.indexOf(previousScreen)
            val currIdx = screenOrder.indexOf(selected)
            val goingRight = currIdx >= prevIdx

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(horizontal = 16.dp, vertical = 12.dp)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                AnimatedContent(
                    targetState = selected,
                    transitionSpec = {
                        (slideInHorizontally(
                            initialOffsetX = { if (goingRight) it else -it },
                            animationSpec = spring(Spring.DampingRatioMediumBouncy, Spring.StiffnessMediumLow)
                        ) + fadeIn(tween(220))) togetherWith
                        (slideOutHorizontally(
                            targetOffsetX = { if (goingRight) -it / 3 else it / 3 },
                            animationSpec = tween(200)
                        ) + fadeOut(tween(160)))
                    },
                    label = "screen"
                ) { screen ->
                    when (screen) {
                        DemoScreen.AUTH -> AuthScreen(onAuthenticated = {
                            previousScreen = selected
                            selected = DemoScreen.HOME
                        })
                        DemoScreen.HOME -> HomeScreen(onGoToSos = {
                            previousScreen = selected
                            selected = DemoScreen.ACTIVE_SOS
                        })
                        DemoScreen.ACTIVE_SOS -> ActiveSosScreen(
                            onStop = {
                                previousScreen = selected
                                selected = DemoScreen.HOME
                            },
                            onExtend = {}
                        )
                        DemoScreen.RESPONDER_ALERT -> ResponderAlertScreen()
                        DemoScreen.HISTORY -> HistoryScreen(incidents = demoIncidents)
                    }
                }
                Spacer(Modifier.height(8.dp))
            }
        }
    }
}