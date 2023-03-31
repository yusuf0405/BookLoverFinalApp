package com.example.bookloverfinalapp.app.ui.general_screens.screen_main.models

import com.example.bookloverfinalapp.app.models.SavedStatus
import com.joseph.ui_core.adapter.Item
import com.example.bookloverfinalapp.app.ui.general_screens.screen_main.listeners.BookItemOnClickListener

data class BookAdapterModel(
    val id: String,
    val author: String,
    val title: String,
    val publishYear: String,
    val posterUrl: String,
    val pageCount: Int,
    val savedStatus: SavedStatus,
    val listener: BookItemOnClickListener
) : Item