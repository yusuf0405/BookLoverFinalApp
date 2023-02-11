package com.example.data.cloud.mappers

import com.example.data.cloud.models.BookCloud
import com.example.data.models.BookData
import com.example.data.models.BookPdfData
import com.example.data.models.BookPosterData
import com.example.data.models.SavedStatusData
import com.example.domain.Mapper

interface BookCloudDataMapper {
    fun map(from: BookCloud, cachedBooksIds: List<String> = emptyList()): BookData

    fun map(): Mapper<BookCloud, BookData>
}

class BookCloudDataMapperImpl : BookCloudDataMapper {
    override fun map(from: BookCloud, cachedBooksIds: List<String>) =
        createBookData(from, cachedBooksIds)

    override fun map() = object : Mapper<BookCloud, BookData> {
        override fun map(from: BookCloud): BookData = createBookData(from, emptyList())
    }


    private fun createBookData(bookCloud: BookCloud, cachedBooksIds: List<String>) = bookCloud.run {
        BookData(
            id = id,
            author = author,
            createdAt = createdAt,
            page = page,
            book = BookPdfData(
                name = book.name,
                type = book.type,
                url = book.url
            ),
            title = title,
            chapterCount = chapterCount,
            poster = BookPosterData(
                name = poster.name,
                url = poster.url
            ),
            updatedAt = updatedAt,
            publicYear = publicYear,
            genres = genres,
            description = description,
            savedStatus = handleSavedStatus(currentId = id, cachedBooksIds = cachedBooksIds)
        )
    }

    private fun handleSavedStatus(cachedBooksIds: List<String>, currentId: String) =
        if (cachedBooksIds.contains(currentId)) SavedStatusData.SAVED
        else SavedStatusData.NOT_SAVED
}

