package com.example.myislam.api

import com.example.myislam.Constants.ARABIC_LANG_CODE
import com.example.myislam.Constants.LANGUAGE_PARAM
import com.example.myislam.Constants.RADIOS_ENDPOINT
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query


interface RadiosWebService {

    @GET(RADIOS_ENDPOINT)
    fun getRadios(
        @Query(LANGUAGE_PARAM) language: String = ARABIC_LANG_CODE
    ): Call<RadioResponse>
}