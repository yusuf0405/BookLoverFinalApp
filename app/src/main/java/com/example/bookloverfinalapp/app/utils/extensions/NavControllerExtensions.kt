package com.example.bookloverfinalapp.app.utils.extensions

import androidx.navigation.NavController
import com.example.bookloverfinalapp.app.utils.navigation.NavCommand

fun NavController.navigateTo(navCommand: NavCommand) {
    navigate(navCommand.resId, navCommand.args)
}
