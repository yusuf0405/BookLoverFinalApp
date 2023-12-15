package com.example.bookloverfinalapp.app.ui.general_screens.screen_main.models

import com.joseph.ui.core.adapter.Item
import com.example.bookloverfinalapp.app.ui.general_screens.screen_main.listeners.BookItemOnClickListener

data class ExclusiveBookItem(
    val id: String,
    val posterUrl: String,
    val title: String,
    val description: String,
    val listener: BookItemOnClickListener
) : Item