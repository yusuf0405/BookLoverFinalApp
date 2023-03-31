package com.example.bookloverfinalapp.app.ui.general_screens.screen_main.models

import com.joseph.ui_core.adapter.Item
import com.example.bookloverfinalapp.app.ui.general_screens.screen_main.listeners.GenreItemOnClickListeners

data class GenreAdapterModel(
    val genreCode: String,
    val genreTitle: String,
    val genreBooksSize: Int,
    val listeners: GenreItemOnClickListeners
) : Item