package com.example.myislam.api.model

import android.util.Log
import com.example.myislam.Constance
import com.google.gson.Gson
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ApiManager {
    companion object {
        private var retrofit: Retrofit? = null

        fun getInstance(): Retrofit {

            val httpLoggingInterceptor =
                HttpLoggingInterceptor(object : HttpLoggingInterceptor.Logger {
                    override fun log(message: String) {
                        Log.e("api", message)
                    }
                })
            val okHttpClient =
                OkHttpClient.Builder().addInterceptor(HttpLoggingInterceptor()).build()

            if (retrofit == null) {
                retrofit = Retrofit.Builder().client(okHttpClient).baseUrl(Constance.BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
            }
            return retrofit!!
        }

    }
}
