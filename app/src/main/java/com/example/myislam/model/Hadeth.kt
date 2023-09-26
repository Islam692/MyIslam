package com.example.myislam.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.io.Serializable

@Parcelize
data class Hadeth(val title: String, val content: String) : Parcelable
