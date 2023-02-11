package com.example.bookloverfinalapp.app.mappers

import com.example.bookloverfinalapp.app.models.*
import com.example.domain.Mapper
import com.example.domain.models.BookDomain
import com.example.domain.models.SavedStatusDomain

class BookDomainToBookMapper : Mapper<BookDomain, Book> {
    override fun map(from: BookDomain): Book = from.run {
        Book(
            author = author,
            createdAt = createdAt,
            id = id,
            page = page,
            publicYear = publicYear,
            title = title,
            chapterCount = chapterCount,
            poster = BookPoster(name = poster.name, url = poster.url), updatedAt = updatedAt,
            bookPdf = BookPdf(name = book.name, url = book.url),
            genreIds = genreIds,
            description = description,
            savedStatus = mapSavedStatus(savedStatus)
        )
    }

    private fun mapSavedStatus(status: SavedStatusDomain) =
        when (status) {
            SavedStatusDomain.SAVED -> SavedStatus.SAVED
            SavedStatusDomain.NOT_SAVED -> SavedStatus.NOT_SAVED
            SavedStatusDomain.SAVING -> SavedStatus.SAVING
        }
}