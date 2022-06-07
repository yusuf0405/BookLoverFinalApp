package com.example.bookloverfinalapp.app.mappers

import com.example.bookloverfinalapp.app.ui.adapter.BookThatReadModel
import com.example.bookloverfinalapp.app.models.BookThatReadPoster
import com.example.domain.Mapper
import com.example.domain.models.BookThatReadDomain

class BookThatReadDomainToModelMapper : Mapper<BookThatReadDomain, BookThatReadModel> {
    override fun map(from: BookThatReadDomain): BookThatReadModel = from.run {
        BookThatReadModel(
            createdAt = createdAt,
            bookId = bookId,
            page = page,
            publicYear = publicYear,
            book = book,
            title = title,
            chapterCount = chapterCount,
            poster = BookThatReadPoster(url = poster.url, name = poster.name),
            updatedAt = updatedAt,
            objectId = objectId,
            isReadingPages = isReadingPages,
            chaptersRead = chaptersRead,
            progress = progress,
            author = author
        )
    }
}