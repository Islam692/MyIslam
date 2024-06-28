package com.example.myislam.utils

import android.content.Context
import android.widget.Toast
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class Utils @Inject constructor(
    @ApplicationContext private val context: Context
) {

    fun getCurrentLanguageCodeForApi(): String {
        return when (context.resources.configuration.locales[0].language) {
            Constants.ARABIC_LANG_CODE -> Constants.ARABIC_LANG_CODE
            else -> Constants.API_ENGLISH_LANG_CODE
        }
    }

    fun showShortToast(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }
}