package com.example.data.models

import java.util.*

class BookData(
    var author: String,
    var createdAt: Date,
    var id: String,
    var page: Int,
    var publicYear: String,
    var book: BookPdfData,
    val description: String,
    var title: String,
    var chapterCount: Int,
    var poster: BookPosterData,
    var updatedAt: Date,
    var genres: List<String>,
    val savedStatus: SavedStatusData = SavedStatusData.NOT_SAVED
) {
    companion object {
        val unknown = BookData(
            id = String(),
            author = String(),
            publicYear = String(),
            title = String(),
            description = String(),
            createdAt = Date(),
            updatedAt = Date(), page = 0,
            genres = emptyList(),
            book = BookPdfData(String(), String(), String()),
            chapterCount = 0,
            poster = BookPosterData(String(), String())
        )
    }
}

enum class SavedStatusData {
    SAVED,
    NOT_SAVED,
    SAVING
}

data class BookPdfData(
    var name: String,
    var type: String,
    var url: String,
)

data class BookPosterData(
    var name: String,
    var url: String,
)
