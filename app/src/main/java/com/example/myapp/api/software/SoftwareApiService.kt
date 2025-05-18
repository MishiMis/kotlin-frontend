package com.example.myapp.api.software


import com.example.myapp.model.evaluacion.EvaluationRequest
import com.example.myapp.model.parametro.Parametro
import com.example.myapp.model.parametro.ParametroListResponse
import com.example.myapp.model.parametro.ParametroRequest
import com.example.myapp.model.sofware.Software
import com.example.myapp.model.sofware.SoftwareListResponse
import com.example.myapp.model.sofware.SoftwareRequest
import retrofit2.Response
import retrofit2.http.*

interface SoftwareApiService {
    @GET("software")
    suspend fun getSoftwareList(): SoftwareListResponse

    @POST("software")
    suspend fun createSoftware(@Body software: SoftwareRequest): Software

    @GET("parameters")
    suspend fun getParametros(): ParametroListResponse

    @POST("parameters")
    suspend fun createParametros(@Body parameter: ParametroRequest): Parametro

    @POST("evaluations")
    suspend fun createEvaluation(@Body evaluation: EvaluationRequest): Response<Void>

}