package com.example.data.cloud.mappers

import com.example.data.cloud.models.BookCloud
import com.example.data.cloud.models.BookPdfCloud
import com.example.data.cloud.models.BookPosterCloud
import com.example.data.cloud.models.BookResponse
import com.example.domain.Mapper
import javax.inject.Inject

class BookResponseToBookCloudMapper @Inject constructor() : Mapper<BookResponse, BookCloud> {

    override fun map(from: BookResponse): BookCloud {
        if (from.books.isEmpty()) return BookCloud.unknown
        val book = from.books.first()
        return BookCloud(
            id = book.id,
            author = book.author,
            publicYear = book.publicYear,
            title = book.title,
            createdAt = book.createdAt,
            updatedAt = book.updatedAt,
            page = book.page,
            genres = book.genres,
            book = BookPdfCloud(
                name = book.book?.name ?: String(),
                url = book.book?.url ?: String(),
                type = book.book?.type ?: String()
            ),
            chapterCount = book.chapterCount,
            poster = BookPosterCloud(
                name = book.poster?.name ?: String(),
                url = book.poster?.url ?: String(),
                type = book.poster?.type ?: String()
            ),
            description = book.description,
            isExclusive = book.isExclusive
        )
    }
}