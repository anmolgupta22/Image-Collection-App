package com.example.imagecollectionapp.util

import android.util.Log
import retrofit2.Response

/**
 * Retrofit only gives generic response body when status is Successful.
 * This extension will also parse error body and will give generic response.
 */
inline fun <reified T> Response<T>.getResponse(): T {
    val responseBody = body()
    Log.d(
        "RetrofitUtils",
        "impl, getResponse: responseBody = $responseBody"
    )
    return if (this.isSuccessful && responseBody != null) {
        responseBody
    } else {
        fromJson<T>(errorBody()!!.string())!!
    }
}

