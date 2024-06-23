package com.example.myislam.api.model

import retrofit2.Call
import retrofit2.http.GET

interface WebService {
    @GET("radios")
    fun getRadio(): Call<RadioResponse>
}