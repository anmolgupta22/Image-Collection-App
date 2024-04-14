package com.example.imagecollectionapp.util

sealed class ViewState<T> {
    class Loading<T> : ViewState<T>()
    class Success<T>(val data: T) : ViewState<T>()
    class Failed<T>(val message: String, val code: Int) : ViewState<T>()
    class NetworkFailed<T>(val message: String) : ViewState<T>()
    class ExceptionError<T>(val message: String) : ViewState<T>()
    class ErrorsData<T>(val errorData: T) : ViewState<T>()
    companion object {
        fun <T> loading() = Loading<T>()
        fun <T> success(data: T) = Success(data)
        fun <T> failed(message: String, code: Int = 0) = Failed<T>(message, code)
        fun <T> networkFailed(networkMessage: String) = NetworkFailed<T>(networkMessage)
        fun <T> exceptionError(exception: String) = ExceptionError<T>(exception)
        fun <T> errorData(errorData: T) = ErrorsData<T>(errorData)
    }
}
