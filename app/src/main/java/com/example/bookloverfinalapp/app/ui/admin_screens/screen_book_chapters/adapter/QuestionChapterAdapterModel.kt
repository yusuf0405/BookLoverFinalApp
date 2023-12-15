package com.example.bookloverfinalapp.app.ui.admin_screens.screen_book_chapters.adapter

import com.joseph.ui.core.adapter.Item

data class QuestionChapterAdapterModel(
    val title: String,
    val chapter: Int,
    val actionListener: QuesionChapterItemOnClickListener,
) : Item