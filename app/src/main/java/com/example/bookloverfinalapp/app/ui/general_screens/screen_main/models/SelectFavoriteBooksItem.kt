package com.example.bookloverfinalapp.app.ui.general_screens.screen_main.models

import com.joseph.ui.core.adapter.Item

data class SelectFavoriteBooksItem(
    val listener: SelectFavoriteBooksItemOnClickListeners
) : Item

interface SelectFavoriteBooksItemOnClickListeners {

    fun onClickSelectFavoriteBook()
}