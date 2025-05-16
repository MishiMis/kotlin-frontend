@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.myapp.ui.sidebar

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.myapp.navigation.AppNavHost
import com.example.myapp.navigation.Screens
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.ui.draw.shadow

@Composable
fun Sidebar() {
    val navController = rememberNavController()
    var isSidebarVisible by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { },
                navigationIcon = {
                    IconButton(
                        onClick = { isSidebarVisible = !isSidebarVisible }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Menu,
                            contentDescription = "Menú",
                            tint = MaterialTheme.colorScheme.inversePrimary
                        )
                    }
                },
                actions = {

                    IconButton(onClick = { /* Acción de búsqueda */ }) {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = "Buscar",
                            tint = MaterialTheme.colorScheme.inversePrimary
                        )
                    }

                    BadgedBox(
                        badge = {
                            Badge(
                                containerColor = MaterialTheme.colorScheme.error,
                                contentColor = MaterialTheme.colorScheme.onError
                            ) {
                                Text("3")
                            }
                        }
                    ) {
                        IconButton(onClick = { /* Acción de notificaciones */ }) {
                            Icon(
                                imageVector = Icons.Default.Notifications,
                                contentDescription = "Notificaciones",
                                tint = MaterialTheme.colorScheme.inversePrimary
                            )
                        }
                    }

                    IconButton(onClick = { /* Acción de configuración */ }) {
                        Icon(
                            imageVector = Icons.Default.Settings,
                            contentDescription = "Configuración",
                            tint = MaterialTheme.colorScheme.inversePrimary
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    navigationIconContentColor = MaterialTheme.colorScheme.onSurface,
                    actionIconContentColor = MaterialTheme.colorScheme.onSurface,
                    titleContentColor = MaterialTheme.colorScheme.onSurface
                ),
                modifier = Modifier.shadow(elevation = 4.dp)
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            AppNavHost(navController = navController)

            if (isSidebarVisible) {
                SidebarOverlay(
                    navController = navController,
                    onClose = { isSidebarVisible = false }
                )
            }
        }
    }
}

@Composable
fun SidebarOverlay(
    navController: NavHostController,
    onClose: () -> Unit,
    modifier: Modifier = Modifier
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    Box(
        modifier = Modifier
            .fillMaxSize()
            .clickable(onClick = onClose)
            .background(Color.Black.copy(alpha = 0.5f))
    ) {
        Surface(
            modifier = modifier
                .fillMaxHeight()
                .width(280.dp)
                .zIndex(1f),
            color = MaterialTheme.colorScheme.surface,
            shadowElevation = 8.dp
        ) {
            Column {

                SidebarItem(
                    text = "Dashboard",
                    isSelected = currentRoute == Screens.Dashboard.route,
                    onClick = {
                        navController.navigate(Screens.Dashboard.route) {
                            launchSingleTop = true
                            restoreState = true
                        }
                        onClose()
                    }
                )
                SidebarItem(
                    text = "Productos",
                    isSelected = currentRoute == Screens.Productos.route,
                    onClick = {
                        navController.navigate(Screens.Productos.route) {
                            launchSingleTop = true
                            restoreState = true
                        }
                        onClose()
                    }
                )
            }
        }
    }
}