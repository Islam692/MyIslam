package com.example.myislam.api

import com.example.myislam.Constants
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiManager {
    private var retrofit: Retrofit? = null

    fun getRadiosService(): RadiosWebService {
        if (retrofit == null) {
            retrofit = Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(Constants.RADIOS_WEB_API_URL)
                .build()
        }

        return retrofit!!.create(RadiosWebService::class.java)
    }
}