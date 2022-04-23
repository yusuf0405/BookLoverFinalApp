package com.example.bookloverfinalapp.app.ui.student_screens.screen_all_books.mappers

import com.example.domain.domain.Mapper
import com.example.domain.domain.models.BookDomain
import com.example.bookloverfinalapp.app.models.BookAdapterModel
import com.example.bookloverfinalapp.app.models.BookPdfAdapter
import com.example.bookloverfinalapp.app.models.BookPosterAdapter

class BookAdapterModelMapper : Mapper<BookDomain, BookAdapterModel.Base>() {
    override fun map(from: BookDomain): BookAdapterModel.Base = from.run {
        BookAdapterModel.Base(
            title = title,
            author = author,
            updatedAt = updatedAt,
            page = page,
            createdAt = createdAt,
            chapterCount = chapterCount,
            publicYear = publicYear,
            poster = BookPosterAdapter(url = poster.url, name = poster.name),
            book = BookPdfAdapter(url = book.url, name = book.name, type = book.type),
            id = id
        )
    }
}