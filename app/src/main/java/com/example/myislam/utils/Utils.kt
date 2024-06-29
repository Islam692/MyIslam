package com.example.myislam.utils

import android.app.Activity
import android.content.Context
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder
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

    companion object {
        fun Fragment.createDialog(
            title: String,
            message: String,
            posBtnText: String? = null,
            posBtnAction: (() -> Unit)? = null,
            negBtnText: String? = null,
            negBtnAction: (() -> Unit)? = null,
            isCancelable: Boolean = false
        ): AlertDialog {
            return MaterialAlertDialogBuilder(requireContext())
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton(posBtnText) { _, _ ->
                    posBtnAction?.invoke()
                }
                .setNegativeButton(negBtnText) { _, _ ->
                    negBtnAction?.invoke()
                }
                .setCancelable(isCancelable)
                .create()
        }

        fun Activity.createDialog(
            title: String,
            message: String,
            posBtnText: String? = null,
            posBtnAction: (() -> Unit)? = null,
            negBtnText: String? = null,
            negBtnAction: (() -> Unit)? = null,
            isCancelable: Boolean = false
        ): AlertDialog {
            return MaterialAlertDialogBuilder(this)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton(posBtnText) { _, _ ->
                    posBtnAction?.invoke()
                }
                .setNegativeButton(negBtnText) { _, _ ->
                    negBtnAction?.invoke()
                }
                .setCancelable(isCancelable)
                .create()
        }
    }
}
