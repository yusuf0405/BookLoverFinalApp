package com.example.data.cloud.models

import com.google.gson.annotations.SerializedName
import java.util.*

data class BookResponse(
    @SerializedName("results")
    var books: List<BookCloud>,
)

data class BookCloud(
    @SerializedName("author") var author: String,
    @SerializedName("description") val description: String,
    @SerializedName("createdAt") var createdAt: Date,
    @SerializedName("objectId") var id: String,
    @SerializedName("page") var page: Int,
    @SerializedName("genres") var genres: List<String>,
    @SerializedName("publicYear") var publicYear: String,
    @SerializedName("book") var book: BookPdfCloud,
    @SerializedName("title") var title: String,
    @SerializedName("chapterCount") var chapterCount: Int,
    @SerializedName("poster") var poster: BookPosterCloud,
    @SerializedName("updatedAt") var updatedAt: Date,
) {

    companion object {
        val unknown = BookCloud(
            id = String(),
            author = String(),
            publicYear = String(),
            title = String(),
            description = String(),
            createdAt = Date(),
            updatedAt = Date(), page = 0,
            genres = emptyList(),
            book = BookPdfCloud(String(), String(), String()),
            chapterCount = 0,
            poster = BookPosterCloud(String(), String(), String())
        )
    }
}

data class BookPdfCloud(
    @SerializedName("name") var name: String,
    @SerializedName("__type") var type: String,
    @SerializedName("url") var url: String,
)

data class BookPosterCloud(
    @SerializedName("name") var name: String,
    @SerializedName("__type") var type: String,
    @SerializedName("url") var url: String,
)
