package com.example.imagecollectionapp.repository

import com.google.gson.JsonObject
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query


interface UnsplashService {

    @GET("photos")
    suspend fun getUnsplashImages(
        @Query("client_id") clientId: String,
        @Query("page") page: Int?,
    ): Response<List<JsonObject>>
}