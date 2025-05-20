@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.myapp.ui.sidebar

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Build
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontWeight

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Sidebar() {
    val navController = rememberNavController()
    var isSidebarVisible by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "MiApp",
                        style = MaterialTheme.typography.titleLarge.copy(
                            color = Color.White,
                            fontWeight = FontWeight.Bold
                        )
                    )
                },
                navigationIcon = {
                    IconButton(
                        onClick = { isSidebarVisible = !isSidebarVisible }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Menu,
                            contentDescription = "Menú",
                            tint = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF1565C0), // Azul oscuro
                    titleContentColor = Color.White,
                    actionIconContentColor = Color.White
                ),
                modifier = Modifier.shadow(elevation = 8.dp)
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
    onClose: () -> Unit
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    // Gradiente azul para el fondo
    val gradient = Brush.verticalGradient(
        colors = listOf(
            Color(0xFF1976D2),  // Azul medio
            Color(0xFF0D47A1)   // Azul oscuro
        )
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.3f))
            .clickable(onClick = onClose)
    ) {
        Surface(
            modifier = Modifier
                .fillMaxHeight()
                .width(300.dp)
                .offset(x = (-16).dp) // Pequeño efecto flotante
                .zIndex(1f),
            shape = RoundedCornerShape(
                topEnd = 0.dp,
                bottomEnd = 24.dp
            ),
            shadowElevation = 16.dp,
            color = Color.Transparent
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(gradient)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(vertical = 24.dp)
                ) {
                    // Header del sidebar
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 24.dp, vertical = 16.dp)
                    ) {
                        Text(
                            text = "Menú",
                            style = MaterialTheme.typography.headlineSmall.copy(
                                color = Color.White,
                                fontWeight = FontWeight.Bold
                            )
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Items del menú
                    val menuItems = listOf(
                        Screens.Residuos,
                        Screens.Recoleccion
                    )

                    menuItems.forEach { screen ->
                        val isSelected = currentRoute == screen.route

                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(
                                    if (isSelected) Color.White.copy(alpha = 0.2f)
                                    else Color.Transparent
                                )
                                .clickable {
                                    navController.navigate(screen.route) {
                                        launchSingleTop = true
                                        restoreState = true
                                    }
                                    onClose()
                                }
                                .padding(vertical = 12.dp, horizontal = 24.dp)
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(40.dp)
                                        .background(
                                            if (isSelected) Color.White
                                            else Color.White.copy(alpha = 0.2f),
                                            shape = CircleShape
                                        )
                                        .padding(8.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = when(screen) {
                                            Screens.Residuos -> Icons.Default.Build
                                            Screens.Recoleccion -> Icons.Default.Build
                                            else -> Icons.Default.Build
                                        },
                                        contentDescription = screen.route,
                                        tint = if (isSelected)
                                            Color(0xFF0D47A1)
                                        else
                                            Color.White,
                                        modifier = Modifier.size(20.dp)
                                    )
                                }

                                Spacer(modifier = Modifier.width(16.dp))

                                Text(
                                    text = when(screen) {

                                        Screens.Residuos -> "Residuos"
                                        Screens.Recoleccion -> "Recolección"
                                        else -> ""
                                    },
                                    style = MaterialTheme.typography.bodyLarge.copy(
                                        color = Color.White,
                                        fontWeight = if (isSelected)
                                            FontWeight.Bold
                                        else
                                            FontWeight.Normal
                                    )
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.weight(1f))

                    // Footer del sidebar
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    ) {
                        Text(
                            text = "Versión 1.0.0",
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = Color.White.copy(alpha = 0.7f)
                            )
                        )
                    }
                }
            }
        }
    }
}