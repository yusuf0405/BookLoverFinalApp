package com.example.data.mappers

import com.example.data.models.BookData
import com.example.data.models.SavedStatusData
import com.example.domain.Mapper
import com.example.domain.models.BookDomain
import com.example.domain.models.BookPdfDomain
import com.example.domain.models.BookPosterDomain
import com.example.domain.models.SavedStatusDomain

class BookDataToDomainMapper : Mapper<BookData, BookDomain> {
    override fun map(from: BookData): BookDomain = from.run {
        if (from == BookData.unknown) BookDomain.unknown
        else BookDomain(
            author = author,
            createdAt = createdAt,
            page = page,
            book = BookPdfDomain(
                name = book.name,
                type = book.type,
                url = book.url
            ),
            title = title,
            chapterCount = chapterCount,
            poster = BookPosterDomain(
                name = poster.name,
                url = poster.url
            ),
            updatedAt = updatedAt, id = id,
            publicYear = publicYear,
            genreIds = genres,
            description = description,
            savedStatus = mapSavedStatus(savedStatus),
            isExclusive = isExclusive
        )
    }

    private fun mapSavedStatus(status: SavedStatusData) =
        when (status) {
            SavedStatusData.SAVED -> SavedStatusDomain.SAVED
            SavedStatusData.NOT_SAVED -> SavedStatusDomain.NOT_SAVED
            SavedStatusData.SAVING -> SavedStatusDomain.SAVING
        }
}