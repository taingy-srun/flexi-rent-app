package com.roomrental.android.data.api

import android.content.Context
import com.google.gson.GsonBuilder
import com.roomrental.android.utils.PreferenceManager
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object ApiClient {
    private const val BASE_URL = "http://192.168.0.182:8080/"

    private var retrofit: Retrofit? = null
    private var preferenceManager: PreferenceManager? = null

    fun initialize(context: Context) {
        preferenceManager = PreferenceManager(context)
    }

    private fun getAuthInterceptor(): Interceptor {
        return Interceptor { chain ->
            val token = preferenceManager?.getAuthToken()
            println("ApiClient: Auth token in interceptor: ${token != null}")
            val request = if (token != null) {
                println("ApiClient: Adding Authorization header with token")
                chain.request().newBuilder()
                    .addHeader("Authorization", "Bearer $token")
                    .build()
            } else {
                println("ApiClient: No token available, proceeding without Authorization header")
                chain.request()
            }
            println("ApiClient: Request URL: ${request.url}")
            println("ApiClient: Request headers: ${request.headers}")
            chain.proceed(request)
        }
    }

    private fun getOkHttpClient(): OkHttpClient {
        val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

        return OkHttpClient.Builder()
            .addInterceptor(getAuthInterceptor())
            .addInterceptor(loggingInterceptor)
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .build()
    }

    private fun getGson() = GsonBuilder()
        .setLenient()
        .create()

    private fun getRetrofit(): Retrofit {
        if (retrofit == null) {
            retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(getOkHttpClient())
                .addConverterFactory(GsonConverterFactory.create(getGson()))
                .build()
        }
        return retrofit!!
    }

    fun getApiService(): ApiService {
        return getRetrofit().create(ApiService::class.java)
    }
}