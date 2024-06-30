package com.example.myislam.data.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class QuranSura(
    val name: String,
    val ayaCount: Int,
    val number: Int,
    val type: String
) : Parcelable
