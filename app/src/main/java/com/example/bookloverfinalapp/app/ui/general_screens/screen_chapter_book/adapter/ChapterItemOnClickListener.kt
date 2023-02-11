package com.example.bookloverfinalapp.app.ui.general_screens.screen_chapter_book.adapter

import com.shockwave.pdfium.PdfDocument

interface ChapterItemOnClickListener {

    fun chapterItemOnClickListener(chapter: PdfDocument.Bookmark, currentChapterPosition: Int)

}