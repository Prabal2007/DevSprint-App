package com.example.myapplication

import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {

    @GET("generate_insult.php?lang=en&type=json")
    suspend fun getInsult(@Query("nocache") nocache: Long): InsultResponse

    @GET("advice")
    suspend fun getAdvice(@Query("nocache") nocache: Long): AdviceResponse

    @GET(".")
    suspend fun getKanye(@Query("nocache") nocache: Long): KanyeResponse

    @GET("api")
    suspend fun getYesNo(@Query("nocache") nocache: Long): YesNoResponse
}