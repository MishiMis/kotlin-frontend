package com.example.myapp.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.myapp.ui.residuos.ResiduosScreen
import com.example.myapp.ui.recoleccion.RecoleccionScreen


@Composable
fun AppNavHost(
    navController: NavHostController = rememberNavController()
) {
    NavHost(
        navController = navController,
        startDestination = Screens.Residuos.route
    ) {
        composable(Screens.Residuos.route){
            ResiduosScreen()
        }
        composable(Screens.Recoleccion.route){
            RecoleccionScreen()
        }
    }
}