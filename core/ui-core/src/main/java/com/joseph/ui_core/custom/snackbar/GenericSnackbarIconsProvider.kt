package com.joseph.ui_core.custom.snackbar

import android.content.Context
import android.graphics.drawable.Drawable
import androidx.core.content.ContextCompat
import com.joseph.ui_core.R

interface GenericSnackbarIconsProvider {

    fun infoSnackbarIcon(): Drawable?

    fun successSnackbarIcon(): Drawable?

    fun warningSnackbarIcon(): Drawable?

    fun errorSnackbarIcon(): Drawable?

    class Base(private val context: Context) : GenericSnackbarIconsProvider {

        override fun infoSnackbarIcon(): Drawable? =
            ContextCompat.getDrawable(context, R.drawable.ic_generic_snackbar_info)

        override fun successSnackbarIcon(): Drawable? =
            ContextCompat.getDrawable(context, R.drawable.ic_generic_snackbar_success)

        override fun warningSnackbarIcon(): Drawable? =
            ContextCompat.getDrawable(context, R.drawable.ic_generic_snackbar_warning)

        override fun errorSnackbarIcon(): Drawable? =
            ContextCompat.getDrawable(context, R.drawable.ic_generic_snackbar_error)
    }
}