package com.example.bookloverfinalapp.app.ui.general_screens.screen_main.models

import com.example.bookloverfinalapp.app.ui.general_screens.screen_main.adapter.base.Item
import com.example.bookloverfinalapp.app.ui.general_screens.screen_main.listeners.ErrorItemOnClickListener


data class MainScreenErrorItem(
    val errorMessage: String,
    val listener: ErrorItemOnClickListener
) : Item