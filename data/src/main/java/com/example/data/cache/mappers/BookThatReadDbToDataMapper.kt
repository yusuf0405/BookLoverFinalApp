package com.example.data.cache.mappers

import com.example.data.cache.models.BookThatReadDb
import com.example.data.models.BookThatReadData
import com.example.data.models.BookThatReadPosterData
import com.example.domain.Mapper

class BookThatReadDbToDataMapper : Mapper<BookThatReadDb, BookThatReadData>() {

    override fun map(from: BookThatReadDb): BookThatReadData = from.run {
        BookThatReadData(
            author = author,
            createdAt = createdAt,
            bookId = bookId,
            objectId = objectId,
            page = page,
            publicYear = publicYear,
            title = title,
            chapterCount = chapterCount,
            chaptersRead = chaptersRead,
            updatedAt = updatedAt,
            poster = BookThatReadPosterData(name = poster.name, url = poster.url),
            book = book,
            isReadingPages = isReadingPages,
            progress = progress
        )
    }
}