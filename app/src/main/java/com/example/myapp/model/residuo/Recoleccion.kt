
package com.example.myapp.model.residuo

import com.google.gson.annotations.SerializedName
import java.util.Date

data class Recoleccion(
    @SerializedName("_id") val id: String? = null,
    val fecha: String,
    val ubicacion: Ubicacion,
    val items: List<ItemRecoleccion>,
    val responsable: String,
    val estado: String = "completado"
)

data class Ubicacion(
    val direccion: String
)

data class ItemRecoleccion(
    val tipoResiduo: String,
    val cantidad: Double,
    val unidad: String,
    val observaciones: String? = null
)

data class RecoleccionRequest(
    val fecha: String,
    val ubicacion: Ubicacion,
    val items: List<ItemRecoleccion>,
    val responsable: String,
    val estado: String = "completado"
)