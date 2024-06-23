package com.example.myislam.api.model

import com.google.gson.annotations.SerializedName

class RadioResponse(
    @field:SerializedName("radio")
    val radios: List<Radio?>? = null
)