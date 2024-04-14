package com.example.imagecollectionapp.repository

import android.util.Log
import com.example.imagecollectionapp.util.Constants
import com.example.imagecollectionapp.util.ResponseResult
import com.example.imagecollectionapp.util.getResponse
import com.google.gson.JsonObject
import retrofit2.HttpException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UnsplashRepository @Inject constructor(
    private val unsplashService: UnsplashService,
) : UnsplashRepositoryImpl {

    // Function to fetch Unsplash images from the API
    override suspend fun getUnsplashImages(page: Int?): ResponseResult<List<JsonObject>> {
        return try {
            // Make API call using UnsplashService to get images
            val response = unsplashService.getUnsplashImages(
                "nGD3rUeiO7oXFgN5H4BfUU4CNVOAFPLrB2ufsxUL8Fo",
                page
            ).getResponse()
            Log.d("UnsplashRepository", "getUnsplashImages: check the response $response")

            // Check if the response is not empty
            when (response.isNotEmpty()) {
                true -> ResponseResult.success(response) // Return success result with images list
                else -> ResponseResult.error(response.toString()) // Return error result if response is empty
            }
        } catch (e: HttpException) {
            // Handle HttpException (network-related error)
            ResponseResult.networkError(Constants.ERROR_MESSAGE)
        } catch (connection: java.net.ConnectException) {
            // Handle ConnectException (connection-related error)
            ResponseResult.networkError(connection.toString())
        } catch (e: Exception) {
            // Handle other exceptions
            Log.d(
                "UnsplashRepository",
                "#unsplash images initializeObservers getUnsplashAvailable: viewState =, Exception e = $e"
            )
            ResponseResult.ErrorException(e.message.toString()) // Return error result if response is empty
        }
    }
}
