package com.example.imagecollectionapp.repository

import com.example.imagecollectionapp.util.ResponseResult
import com.google.gson.JsonObject
import javax.inject.Singleton


@Singleton
interface UnsplashRepositoryImpl {

    suspend fun getUnsplashImages(
        page: Int?,
    ): ResponseResult<List<JsonObject>>
}