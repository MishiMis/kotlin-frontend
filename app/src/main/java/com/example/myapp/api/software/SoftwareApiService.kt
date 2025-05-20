package com.example.myapp.api.software



import com.example.myapp.model.residuo.Recoleccion
import com.example.myapp.model.residuo.RecoleccionRequest
import com.example.myapp.model.residuo.Residuo
import com.example.myapp.model.residuo.ResiduoListResponse
import com.example.myapp.model.residuo.ResiduoRequest

import retrofit2.Response
import retrofit2.http.*

interface SoftwareApiService {

    @GET("residuos")
    suspend fun getResiduos(): Response<ResiduoListResponse> // Devuelve Response<>

    @POST("residuos")
    suspend fun createResiduo(@Body residuo: ResiduoRequest): Response<Residuo>

    @GET("recolecciones")
    suspend fun getRecolecciones(): Response<List<Recoleccion>>

    @POST("recolecciones")
    suspend fun createRecoleccion(@Body recoleccion: RecoleccionRequest): Response<Recoleccion>

}