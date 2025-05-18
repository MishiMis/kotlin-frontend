package com.example.myapp.api.parametro

import com.example.myapp.model.parametro.Parametro
import com.example.myapp.model.parametro.ParametroListResponse
import com.example.myapp.model.parametro.ParametroRequest
import retrofit2.http.*

interface ParametroApiService {
    @GET("parameters")
    suspend fun getParametros(): ParametroListResponse

    @POST("parameters")
    suspend fun createParametros(@Body parameter: ParametroRequest): Parametro
}