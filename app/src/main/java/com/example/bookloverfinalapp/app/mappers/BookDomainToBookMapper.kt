package com.example.bookloverfinalapp.app.mappers

import com.example.bookloverfinalapp.app.models.Book
import com.example.bookloverfinalapp.app.models.BookPdf
import com.example.bookloverfinalapp.app.models.BookPoster
import com.example.domain.Mapper
import com.example.domain.models.BookDomain

class BookDomainToBookMapper : Mapper<BookDomain, Book>() {
    override fun map(from: BookDomain): Book = from.run {
        Book(author = author,
            createdAt = createdAt,
            objectId = id,
            page = page,
            publicYear = publicYear,
            title = title,
            chapterCount = chapterCount,
            poster = BookPoster(name = poster.name, url = poster.url), updatedAt = updatedAt,
            book = BookPdf(name = book.name, url = book.url), genres = genres)
    }
}