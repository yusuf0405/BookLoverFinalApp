package com.example.bookloverfinalapp.app.ui.general_screens.screen_main.models

import com.joseph.ui.core.adapter.Item

data class AddNewBooksItem(
    val listener: AddNewBooksItemOnClickListeners
) : Item

interface AddNewBooksItemOnClickListeners {

    fun onClickAddNewBook()
}