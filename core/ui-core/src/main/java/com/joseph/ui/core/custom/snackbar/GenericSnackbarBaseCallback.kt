package com.joseph.ui.core.custom.snackbar

import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar

open class GenericSnackbarBaseCallback : BaseTransientBottomBar.BaseCallback<Snackbar>() {

    override fun onShown(transientBottomBar: Snackbar?) = Unit

    override fun onDismissed(transientBottomBar: Snackbar?, event: Int) = Unit
}