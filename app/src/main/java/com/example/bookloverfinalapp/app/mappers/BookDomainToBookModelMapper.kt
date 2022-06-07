package com.example.bookloverfinalapp.app.mappers

import com.example.bookloverfinalapp.app.ui.adapter.BookModel
import com.example.domain.Mapper
import com.example.domain.models.BookDomain

class BookDomainToBookModelMapper(private val viewHolderType: Int) :
    Mapper<BookDomain, BookModel> {
    override fun map(from: BookDomain): BookModel = from.run {
        BookModel(
            title = title,
            author = author,
            updatedAt = updatedAt,
            page = page,
            createdAt = createdAt,
            chapterCount = chapterCount,
            publicYear = publicYear,
            poster = BookModel.BookPosterAdapter(url = poster.url, name = poster.name),
            book = BookModel.BookPdfAdapter(url = book.url, name = book.name, type = book.type),
            id = id,
            genres = genres,
            viewHolderType = viewHolderType
        )
    }
}