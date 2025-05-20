package com.example.myapp.model.residuo

import com.google.gson.annotations.SerializedName

data class Residuo(
    val _id: String,
    val nombre: String,
    val descripcion: String,
    val codigo: String,
    val peligroso: Boolean,
    val unidadBase: String,
    val createdAt: String,
    val updatedAt: String,
    val __v: Int
)

data class ResiduoRequest(
    val nombre: String,
    val descripcion: String,
    val codigo: String,
    val peligroso: Boolean,
    val unidadBase: String
)

typealias ResiduoListResponse = List<Residuo>
