package com.example.myapp.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.myapp.ui.dashboard.DashboardScreen
import com.example.myapp.ui.evaluacion.EvaluacionScreen
import com.example.myapp.ui.software.SoftwareScreen
import com.example.myapp.ui.parametro.ParametrosScreen


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
        composable(Screens.Evaluacion.route) {
            EvaluacionScreen()
        }
        composable(Screens.Software.route){
            SoftwareScreen()
        }
        composable(Screens.Parametros.route){
            ParametrosScreen()
        }
    }
}