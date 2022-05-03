package com.example.bookloverfinalapp.app.mappers

import com.example.bookloverfinalapp.app.models.BookThatReadAdapterModel
import com.example.bookloverfinalapp.app.models.BookThatReadPoster
import com.example.domain.domain.Mapper
import com.example.domain.domain.models.BookThatReadDomain

class BookThatReadAdapterModelMapper : Mapper<BookThatReadDomain, BookThatReadAdapterModel.Base>() {

    override fun map(from: BookThatReadDomain): BookThatReadAdapterModel.Base = from.run {
        BookThatReadAdapterModel.Base(
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