package com.example.bookloverfinalapp.app.ui.general_screens.screen_genre_info.models

import com.example.bookloverfinalapp.app.models.SavedStatus
import com.example.bookloverfinalapp.app.ui.general_screens.screen_main.adapter.base.Item
import com.example.bookloverfinalapp.app.ui.general_screens.screen_main.listeners.BookItemOnClickListener

data class HorizontalBookAdapterModel(
    val id: String,
    val posterUrl: String,
    val title: String,
    val description: String,
    val author: String,
    val savedStatus: SavedStatus,
    val listener: BookItemOnClickListener
) : Item