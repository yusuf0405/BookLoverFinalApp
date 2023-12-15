package com.example.bookloverfinalapp.app.base.sideeffects.snackbar

import android.app.Activity
import android.view.View
import com.joseph.ui.core.custom.snackbar.GenericSnackbar
import javax.inject.Inject

class SnackbarManagerImpl @Inject constructor(
    private val activity: Activity
) : SnackbarManager {

    private val rootView: View by lazy(LazyThreadSafetyMode.NONE) {
        activity.findViewById(android.R.id.content)
    }

    override fun showSuccessSnackbar(message: String) = GenericSnackbar
        .Builder(rootView = rootView)
        .success()
        .message(message)
        .build()
        .show()


    override fun showErrorSnackbar(message: String) = GenericSnackbar
        .Builder(rootView = rootView)
        .error()
        .message(message)
        .build()
        .show()

    override fun showInfoSnackbar(message: String) = GenericSnackbar
        .Builder(rootView = rootView)
        .info()
        .message(message)
        .build()
        .show()
}