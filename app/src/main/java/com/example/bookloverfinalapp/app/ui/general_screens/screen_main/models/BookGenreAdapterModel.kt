package com.example.bookloverfinalapp.app.ui.general_screens.screen_main.models

import com.example.bookloverfinalapp.app.ui.general_screens.screen_main.adapter.base.Item
import com.example.bookloverfinalapp.app.ui.general_screens.screen_main.listeners.BookGenreItemOnClickListeners

data class BookGenreAdapterModel(
    val id: String,
    val titles: List<String>,
    val posterUrl: String,
    val listener: BookGenreItemOnClickListeners
) : Item