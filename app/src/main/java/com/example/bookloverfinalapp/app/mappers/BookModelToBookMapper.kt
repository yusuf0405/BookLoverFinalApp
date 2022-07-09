package com.example.bookloverfinalapp.app.mappers

import com.example.bookloverfinalapp.app.models.BookModel
import com.example.bookloverfinalapp.app.models.Book
import com.example.bookloverfinalapp.app.models.BookPdf
import com.example.bookloverfinalapp.app.models.BookPoster
import com.example.domain.Mapper

class BookModelToBookMapper : Mapper<BookModel, Book> {
    override fun map(from: BookModel): Book = from.run {
        Book(title = title,
            author = author,
            updatedAt = updatedAt,
            page = page,
            createdAt = createdAt,
            chapterCount = chapterCount,
            publicYear = publicYear,
            poster = BookPoster(url = poster.url, name = poster.name),
            book = BookPdf(url = book.url, name = book.name),
            objectId = id,
            genres = genres)
    }
}