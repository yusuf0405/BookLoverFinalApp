package com.example.bookloverfinalapp.app.utils.extensions

import android.util.Log
import androidx.navigation.NavController
import com.example.bookloverfinalapp.app.utils.navigation.NavCommand

fun NavController.navigateTo(navCommand: NavCommand) {
    try {
        navigate(navCommand.resId, navCommand.args)
    } catch (e: java.lang.Exception) {
    }
}
