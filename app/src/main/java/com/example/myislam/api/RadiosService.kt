package com.example.myislam.api

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface RadiosService {

    @GET("radios")
    fun getRadios(
        @Query("language") language: String = "ar"
    ): Call<RadioResponse>
}