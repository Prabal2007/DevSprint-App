package com.example.myapplication

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {

    private val client = OkHttpClient.Builder()
        .addInterceptor { chain ->
            val request = chain.request().newBuilder()
                .header("Cache-Control", "no-cache")
                .header("Pragma", "no-cache")
                .build()
            chain.proceed(request)
        }
        .build()

    val insultApi: ApiService by lazy {
        Retrofit.Builder()
            .baseUrl("https://evilinsult.com/")
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }

    val adviceApi: ApiService by lazy {
        Retrofit.Builder()
            .baseUrl("https://api.adviceslip.com/")
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }

    val kanyeApi: ApiService by lazy {
        Retrofit.Builder()
            .baseUrl("https://api.kanye.rest/")
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }

    val yesNoApi: ApiService by lazy {
        Retrofit.Builder()
            .baseUrl("https://yesno.wtf/")
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }
}