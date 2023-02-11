package com.example.bookloverfinalapp.app.ui.general_screens.screen_all_saved_books.adapter

import com.example.bookloverfinalapp.app.models.BookThatRead

interface SavedBookItemOnClickListeners {

    fun saveBookItemOnClick(savedBook: BookThatRead) = Unit

    fun saveBookItemOnLongClick(savedBookId: String) = Unit

}