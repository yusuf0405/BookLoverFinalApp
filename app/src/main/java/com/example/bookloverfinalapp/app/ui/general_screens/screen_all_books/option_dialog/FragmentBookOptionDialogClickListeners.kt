package com.example.bookloverfinalapp.app.ui.general_screens.screen_all_books.option_dialog

import com.example.bookloverfinalapp.app.models.BookThatRead

interface FragmentBookOptionDialogClickListeners {

    fun addQuestionOnClickListener(bookId: String)

    fun editBookOnClickListener(bookId: String)

    fun bookReadOnClickListener(savedBook:BookThatRead)
}