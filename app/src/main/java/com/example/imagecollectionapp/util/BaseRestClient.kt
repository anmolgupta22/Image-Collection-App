package com.example.imagecollectionapp.util

import okhttp3.Dispatcher
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import java.util.concurrent.TimeUnit

abstract class BaseRestClient {
    companion object {
        const val TIME_OUT = 120
    }

    private val client: OkHttpClient
        get() {
            val builder = OkHttpClient.Builder()
            val okHttpLoggingInterceptor = HttpLoggingInterceptor()
            if (true) {
                okHttpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
            } else {
                okHttpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BASIC
            }
            val dispatcher = Dispatcher()
            dispatcher.maxRequests = 1
            // val okHttpClientBuilder: OkHttpClient.Builder = OkHttpClient.Builder()
            //okHttpClientBuilder.dispatcher(dispatcher)

            builder.connectTimeout(TIME_OUT.toLong(), TimeUnit.SECONDS)
                .readTimeout(TIME_OUT.toLong(), TimeUnit.SECONDS)
                .callTimeout(TIME_OUT.toLong(), TimeUnit.MINUTES)
                .retryOnConnectionFailure(true)
                .followRedirects(true)
                .followSslRedirects(true)
                .addInterceptor(interceptor = SupportInterceptor())
                .addInterceptor(okHttpLoggingInterceptor)
                .writeTimeout(TIME_OUT.toLong(), TimeUnit.SECONDS)
                .dispatcher(dispatcher)
            handleBuilder(builder)
            return builder.build()
        }

    open fun handleBuilder(builder: OkHttpClient.Builder) {}

    fun <T> getServer(cla: Class<T>, baseUrl: String): T {
        val builder = Retrofit.Builder()
        handleConverterFactory(builder)
        return builder.client(client)
            .baseUrl(baseUrl)
            .build()
            .create(cla)
    }

    abstract fun handleConverterFactory(builder: Retrofit.Builder)
}

class SupportInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val authRequest = chain.request().newBuilder().apply {
            header("Content-Type", "application/x-www-form-urlencoded")
        }.build()
        synchronized(this) {
            return chain.proceed(authRequest)
        }

    }

}