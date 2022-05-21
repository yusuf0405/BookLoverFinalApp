package com.example.bookloverfinalapp.app.mappers

import com.example.bookloverfinalapp.app.models.BookAdapterModel
import com.example.bookloverfinalapp.app.models.BookPdfAdapter
import com.example.bookloverfinalapp.app.models.BookPosterAdapter
import com.example.domain.Mapper
import com.example.domain.models.BookDomain

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
            id = id,
            genres = genres
        )
    }
}