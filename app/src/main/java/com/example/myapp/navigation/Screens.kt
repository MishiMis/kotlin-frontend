package com.example.myapp.navigation

sealed class Screens(val route: String) {
    object Dashboard : Screens("dashboard")
    object Evaluacion : Screens("evaluacion")
    object Software : Screens("software")
    object Parametros: Screens("parametros")
}