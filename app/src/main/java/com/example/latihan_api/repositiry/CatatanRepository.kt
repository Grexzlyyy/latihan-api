package com.example.latihan_api.repository

import com.example.latihan_api.entities.Catatan
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface CatatanRepository {
    @POST("catatan")
    suspend fun createCatatan(@Body catatan: Catatan): Response<Catatan>

    @GET("catatan")
    suspend fun getCatatan(): Response<List<Catatan>>

    @GET("catatan/{id}")
    suspend fun getCatatan(@Path("id") id:Int): Response<Catatan>

    @PUT("catatan/{id}")
    suspend fun editCatatan(@Path("id") id:Int, @Body catatan: Catatan): Response<Catatan>
}