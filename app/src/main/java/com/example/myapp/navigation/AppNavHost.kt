package com.example.myapp.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.myapp.ui.dashboard.DashboardScreen
import com.example.myapp.ui.productos.ProductosScreen

@Composable
fun AppNavHost(
    navController: NavHostController = rememberNavController()
) {
    NavHost(
        navController = navController,
        startDestination = Screens.Dashboard.route
    ) {
        composable(Screens.Dashboard.route) {
            DashboardScreen()
        }
        composable(Screens.Productos.route) {
            ProductosScreen()
        }
    }
}