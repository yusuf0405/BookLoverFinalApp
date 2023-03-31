package com.example.data.cache.mappers

import com.example.data.cache.models.BookCache
import com.example.data.cache.models.BookPdfCache
import com.example.data.cache.models.BookPosterCache
import com.example.data.cache.models.SavedStatusCache
import com.example.data.models.BookData
import com.example.data.models.SavedStatusData
import com.example.domain.Mapper

class BookDataToBookDbMapper : Mapper<BookData, BookCache> {

    override fun map(from: BookData): BookCache = from.run {
        BookCache(
            id = id,
            title = title,
            author = author,
            updatedAt = updatedAt,
            page = page,
            createdAt = createdAt,
            chapterCount = chapterCount,
            publicYear = publicYear,
            poster = BookPosterCache(
                name = poster.name,
                url = poster.url
            ),
            book = BookPdfCache(
                name = book.name,
                url = book.url,
                type = book.type
            ),
            description = description,
            genres = genres,
            savedStatus = mapSavedStatus(savedStatus),
            isExclusive = isExclusive
        )
    }

    private fun mapSavedStatus(status: SavedStatusData) =
        when (status) {
            SavedStatusData.SAVED -> SavedStatusCache.SAVED
            SavedStatusData.NOT_SAVED -> SavedStatusCache.NOT_SAVED
            SavedStatusData.SAVING -> SavedStatusCache.SAVING
        }
}