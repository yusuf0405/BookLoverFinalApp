package com.example.bookloverfinalapp.app.ui.admin_screens.screen_book_chapters.adapter

import com.example.bookloverfinalapp.app.ui.general_screens.screen_main.adapter.base.Item

data class QuestionChapterAdapterModel(
    val title: String,
    val chapter: Int,
    val actionListener: QuesionChapterItemOnClickListener,
) : Item