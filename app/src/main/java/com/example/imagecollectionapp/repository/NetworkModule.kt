package com.example.imagecollectionapp.repository

import com.example.imagecollectionapp.util.BaseRestClient
import com.example.imagecollectionapp.util.Constants.BASE_URL
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.Dispatcher
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Named

@Module
@InstallIn(SingletonComponent::class)
class NetworkModule {

    // Provides an instance of HttpLoggingInterceptor for logging HTTP requests and responses
    @Provides
    @Named("image_http_logging_interceptor")
    fun provideHttpLoggingInterceptor(): HttpLoggingInterceptor {
        return HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
    }

    // Provides an OkHttpClient.Builder with custom configurations
    @Provides
    @Named("image_okhttp_client_builder")
    fun provideOkHttpClientBuilder(): OkHttpClient.Builder {
        val dispatcher = Dispatcher()
        dispatcher.maxRequests = 1
        return OkHttpClient.Builder()
            .connectTimeout(BaseRestClient.TIME_OUT.toLong(), TimeUnit.SECONDS)
            .readTimeout(BaseRestClient.TIME_OUT.toLong(), TimeUnit.SECONDS)
            .writeTimeout(BaseRestClient.TIME_OUT.toLong(), TimeUnit.SECONDS)
            .callTimeout(BaseRestClient.TIME_OUT.toLong(), TimeUnit.MINUTES)
            .retryOnConnectionFailure(true)
            .followRedirects(true)
            .followSslRedirects(true)
            .dispatcher(dispatcher)
    }


    // Provides a Retrofit.Builder for creating Retrofit instances
    @Provides
    @Named("image")
    fun provideUnsplashRetrofitBuilder(): Retrofit.Builder {
        return Retrofit.Builder()
            .baseUrl(BASE_URL) // Set the base URL for the API
            .addConverterFactory(GsonConverterFactory.create()) // Use Gson converter for JSON serialization/deserialization
    }

    // Provides an instance of UnsplashService using Retrofit and OkHttpClient
    @Provides
    fun provideUnsplashService(
        @Named("image_http_logging_interceptor") httpLoggingInterceptor: HttpLoggingInterceptor,
        @Named("image") unsplashRetrofitBuilder: Retrofit.Builder,
        @Named("image_okhttp_client_builder") okHttpClientBuilder: OkHttpClient.Builder,
    ): UnsplashService {
        // Add HttpLoggingInterceptor to OkHttpClient for logging purposes
        okHttpClientBuilder.addInterceptor(httpLoggingInterceptor)

        // Build and create the Retrofit instance with OkHttpClient
        return unsplashRetrofitBuilder
            .client(okHttpClientBuilder.build()) // Set the configured OkHttpClient
            .build()
            .create(UnsplashService::class.java) // Create the service interface for API calls
    }
}
