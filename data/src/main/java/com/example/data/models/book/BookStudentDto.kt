package com.example.data.models.book

import com.google.gson.annotations.SerializedName

data class BookStudentDto(
    @SerializedName("author") var author: String,
    @SerializedName("createdAt") var createdAt: String,
    @SerializedName("objectId") var id: String,
    @SerializedName("page") var page: Int,
    @SerializedName("publicYear") var publicYear: String,
    @SerializedName("book") var book: BookPdfDto,
    @SerializedName("title") var title: String,
    @SerializedName("chapterCount") var chapterCount: Int,
    @SerializedName("poster") var poster: BookPosterDto,
    @SerializedName("updatedAt") var updatedAt: String,
    @SerializedName("progress") var progress: Int,
    @SerializedName("isReadingPages") var isReadingPages: List<Boolean>,
)
