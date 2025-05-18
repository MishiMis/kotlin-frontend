package com.example.myapp.api

import com.example.myapp.api.software.SoftwareApiService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {
    private const val BASE_URL = "http://192.168.18.21:3000/"

    val apiService: SoftwareApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(SoftwareApiService::class.java)
    }
}