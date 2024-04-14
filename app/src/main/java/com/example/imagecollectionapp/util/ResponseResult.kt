package com.example.imagecollectionapp.util

sealed class ResponseResult<T> {

    data class Success<T>(val data: T) : ResponseResult<T>()
    data class Error<T>(val message: String, val code: Int) : ResponseResult<T>()
    data class NetworkException<T>(val networkError: String) : ResponseResult<T>()
    data class ErrorException<T>(val exception: String) : ResponseResult<T>()
    data class ErrorData<T>(val errorData: T) : ResponseResult<T>()
    companion object {
        fun <T> success(data: T) = Success(data)
        fun <T> error(message: String, code: Int = 0) = Error<T>(message, code)
        fun <T> networkError(networkError: String) = NetworkException<T>(networkError)
        fun <T> exception(error: String) = ErrorException<T>(error)
        fun <T> errorData(errorData: T) = ErrorData<T>(errorData)

    }
}


