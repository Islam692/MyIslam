package com.example.myislam.data.radio_api.models

import com.google.gson.annotations.SerializedName

data class Radio(
    val id: String? = null,
    val name: String? = null,
    val url: String? = null,
    @SerializedName("recent_date")
    val recentDate: String? = null
)