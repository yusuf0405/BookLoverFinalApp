package com.example.domain.models

import java.util.*

data class BookDomain(
    var author: String,
    var createdAt: Date,
    var id: String,
    var page: Int,
    var publicYear: String,
    var book: BookPdfDomain,
    var title: String,
    val description: String,
    var genreIds: List<String>,
    var chapterCount: Int,
    var poster: BookPosterDomain,
    var updatedAt: Date,
    val savedStatus: SavedStatusDomain
) {
    companion object {
        val unknown = BookDomain(
            id = String(),
            author = String(),
            publicYear = String(),
            description = String(),
            title = String(),
            createdAt = Date(),
            updatedAt = Date(), page = 0,
            genreIds = emptyList(),
            book = BookPdfDomain(String(), String(), String()),
            chapterCount = 0,
            poster = BookPosterDomain(String(), String()),
            savedStatus = SavedStatusDomain.SAVING
        )
    }
}

enum class SavedStatusDomain {
    SAVED,
    NOT_SAVED,
    SAVING
}

class BookPdfDomain(
    var name: String,
    var type: String,
    var url: String,
)

class BookPosterDomain(
    var name: String,
    var url: String,
)