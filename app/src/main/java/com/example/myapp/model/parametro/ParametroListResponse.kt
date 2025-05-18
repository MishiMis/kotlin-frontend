package com.example.myapp.model.parametro

data class ParametroListResponse(
    val statusCode: Int,
    val message: String,
    val data: List<Parametro>
)