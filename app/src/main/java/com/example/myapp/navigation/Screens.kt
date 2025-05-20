package com.example.myapp.navigation

sealed class Screens(val route: String) {

    object Residuos: Screens("residuos")
    object Recoleccion: Screens("recoleccion")

}