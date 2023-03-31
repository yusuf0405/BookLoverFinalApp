package com.example.data.cloud.mappers

import com.example.data.cloud.models.BookCloud
import com.example.data.models.BookData
import com.example.data.models.BookPdfData
import com.example.data.models.BookPosterData
import com.example.data.models.SavedStatusData
import com.example.domain.Mapper
import java.util.*

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
            id = id ?: String(),
            author = author ?: String(),
            createdAt = createdAt ?: Date(),
            page = page ?: 0,
            book = BookPdfData(
                name = book?.name ?: String(),
                type = book?.type ?: String(),
                url = book?.url ?: String()
            ),
            title = title ?: String(),
            chapterCount = chapterCount ?: 0,
            poster = BookPosterData(
                name = poster?.name ?: String(),
                url = poster?.url ?: String()
            ),
            updatedAt = updatedAt ?: Date(),
            publicYear = publicYear ?: String(),
            genres = genres ?: emptyList(),
            description = description ?: String(),
            savedStatus = handleSavedStatus(
                currentId = id ?: String(),
                cachedBooksIds = cachedBooksIds
            ),
            isExclusive = isExclusive ?: false
        )
    }

    private fun handleSavedStatus(cachedBooksIds: List<String>, currentId: String) =
        if (cachedBooksIds.contains(currentId)) SavedStatusData.SAVED
        else SavedStatusData.NOT_SAVED
}

