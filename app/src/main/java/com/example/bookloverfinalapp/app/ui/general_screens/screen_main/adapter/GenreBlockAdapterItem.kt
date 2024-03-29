package com.example.bookloverfinalapp.app.ui.general_screens.screen_main.adapter

import android.os.Parcelable
import com.joseph.ui.core.adapter.Item
import com.example.bookloverfinalapp.app.ui.general_screens.screen_main.listeners.BookGenreItemOnClickListeners

data class GenreBlockAdapterItem(
    val items: List<Item>,
    var state: Parcelable? = null,
    val listeners: BookGenreItemOnClickListeners
) : Item