package com.example.bookloverfinalapp.app.base.sideeffects.snackbar

interface SnackbarManager {

    fun showSuccessSnackbar(message: String)

    fun showErrorSnackbar(message: String)

    fun showInfoSnackbar(message: String)
}