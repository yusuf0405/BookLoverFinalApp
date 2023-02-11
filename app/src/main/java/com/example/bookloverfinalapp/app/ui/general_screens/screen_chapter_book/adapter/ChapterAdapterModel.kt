package com.example.bookloverfinalapp.app.ui.general_screens.screen_chapter_book.adapter

import com.example.bookloverfinalapp.app.ui.general_screens.screen_main.adapter.base.Item
import com.shockwave.pdfium.PdfDocument

data class ChapterAdapterModel(
    val chapter: PdfDocument.Bookmark,
    val chapterIsRead: Boolean,
    val actionListener: ChapterItemOnClickListener,
    val currentChapterPosition: Int,
) : Item