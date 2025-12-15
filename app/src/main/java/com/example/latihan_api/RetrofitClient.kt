package com.example.latihan_api

import com.example.latihan_api.repository.CatatanRepository
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    private const val BASE_URL = "http://192.168.43.137:8000/api/"

    val catatanRepository: CatatanRepository by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(CatatanRepository::class.java)
    }
}