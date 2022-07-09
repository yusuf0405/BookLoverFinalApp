package com.example.bookloverfinalapp.app.mappers

import com.example.bookloverfinalapp.app.models.BookThatReadModel
import com.example.bookloverfinalapp.app.models.BookThatRead
import com.example.bookloverfinalapp.app.models.BookThatReadPoster
import com.example.domain.Mapper

class BookThatReadAdapterModelToBookThatReadMapper :
    Mapper<BookThatReadModel, BookThatRead> {
    override fun map(from: BookThatReadModel): BookThatRead = from.run {
        BookThatRead(createdAt = createdAt,
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
            author = author)
    }
}