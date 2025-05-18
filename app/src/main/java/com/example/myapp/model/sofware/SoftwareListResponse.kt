package com.example.myapp.model.sofware

data class SoftwareListResponse(
    val statusCode: Int,
    val message: String,
    val data: List<Software>
)